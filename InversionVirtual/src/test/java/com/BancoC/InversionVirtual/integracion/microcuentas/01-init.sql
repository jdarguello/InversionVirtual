CREATE TABLE cuenta_bancaria (
    cuenta_id        BIGINT     PRIMARY KEY,
    numero_cuenta    VARCHAR(16)    NOT NULL,
    saldo            NUMERIC(15, 2) DEFAULT 0.00,
    fecha_creacion	 DATE			NOT NULL,
    cliente_id       BIGINT        NOT null
);
   
CREATE TABLE transaccion (
    transaccion_id      BIGINT     PRIMARY KEY,
    cuenta_destino_id   BIGINT        NOT NULL,
    monto               NUMERIC(15, 2) NOT NULL,
    fecha_creacion      DATE          NOT NULL
);

CREATE TABLE transaccion_efectivo (
    transaccion_id      BIGINT        NOT NULL,
    tipo_transaccion    VARCHAR(10)   NOT NULL
);


CREATE TABLE movimiento (
    transaccion_id      BIGINT        NOT NULL,
    cuenta_origen_id   BIGINT        NOT NULL
);