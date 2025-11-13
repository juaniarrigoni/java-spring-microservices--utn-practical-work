-- V2: Agregar tabla historial_estados para seguimiento cronológico de cambios de estado

CREATE TABLE IF NOT EXISTS historial_estados (
    id UUID PRIMARY KEY,
    solicitud_id UUID NOT NULL,
    estado_anterior VARCHAR(50) NOT NULL,
    estado_nuevo VARCHAR(50) NOT NULL,
    fecha_cambio TIMESTAMP NOT NULL,
    observaciones TEXT,
    CONSTRAINT fk_historial_solicitud FOREIGN KEY (solicitud_id) REFERENCES solicitudes(id) ON DELETE CASCADE
);

CREATE INDEX idx_historial_solicitud ON historial_estados(solicitud_id);
CREATE INDEX idx_historial_fecha ON historial_estados(fecha_cambio);

-- Comentario: Esta tabla permite mantener un registro cronológico completo de todos
-- los cambios de estado por los que pasa una solicitud, cumpliendo con el requisito
-- de seguimiento del envío.
