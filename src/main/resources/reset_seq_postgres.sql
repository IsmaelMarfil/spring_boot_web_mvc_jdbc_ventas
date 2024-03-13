SELECT pg_get_serial_sequence('"cliente"', 'id');
ALTER SEQUENCE public.cliente_id_seq RESTART WITH 100;

SELECT pg_get_serial_sequence('"comercial"', 'id');
ALTER SEQUENCE public.comercial_id_seq RESTART WITH 100;

SELECT pg_get_serial_sequence('"pedido"', 'id');
ALTER SEQUENCE public.pedido_id_seq RESTART WITH 100;
