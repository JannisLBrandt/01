CREATE TABLE toDos (
       id BIGSERIAL PRIMARY KEY,
       created_at TIMESTAMP NOT NULL,
       modified_at TIMESTAMP NOT NULL,
       completed_at TIMESTAMP,
       deleted_at TIMESTAMP,
       title TEXT
)
