CREATE TABLE carts (
    id   UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    data JSONB NOT NULL
);
