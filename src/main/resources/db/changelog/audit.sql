CREATE OR REPLACE FUNCTION tf_audit_table()
    RETURNS trigger AS
$BODY$
DECLARE
    query text;
    safe_table_name text;
BEGIN
    SELECT relname
    FROM pg_class cl INNER JOIN pg_namespace nsp ON (cl.relnamespace=nsp.oid)
    WHERE cl.oid=TG_RELID INTO safe_table_name;

    query='INSERT INTO audit_'||safe_table_name||' SELECT ($1).*, now(),$2';

    IF (TG_OP = 'DELETE')
    THEN
        EXECUTE query using OLD,'DELETE';
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        EXECUTE query using OLD,'UPDATE';
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        EXECUTE query using NEW,'INSERT';
        RETURN NEW;
    END IF;

    IF (TG_OP = 'DELETE') THEN RETURN OLD;
    ELSE RETURN NEW;
    END IF;
END;
$BODY$
    LANGUAGE plpgsql VOLATILE SECURITY DEFINER;

create table audit_balance
(
    id serial primary key,
    amount     money   not null,
    currency   varchar not null,
    account_id uuid    not null,
    timestamp timestamp with time zone,
    operation varchar
);

DROP TRIGGER IF exists tr_log_table ON balance;
CREATE TRIGGER tr_audit_table
    BEFORE UPDATE OR DELETE OR INSERT
    ON balance
    FOR EACH ROW
EXECUTE PROCEDURE tf_audit_table();