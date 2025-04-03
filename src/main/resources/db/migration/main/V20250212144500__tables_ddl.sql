-- ✅ Create `advocate` table if it doesn't exist
CREATE TABLE IF NOT EXISTS advocate (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(128) NOT NULL,
    application_number VARCHAR(64) NOT NULL UNIQUE,
    bar_registration_number VARCHAR(64) NOT NULL,
    advocate_type VARCHAR(64) NOT NULL,
    organisation_id UUID,
    individual_id VARCHAR(64),
    is_active BOOLEAN DEFAULT TRUE,
    workflow JSONB,
    documents JSONB,
    audit_details JSONB,
    additional_details JSONB,
    rejection_reason VARCHAR(255),
    created_time BIGINT NOT NULL,
    last_modified_time BIGINT NOT NULL
);

-- ✅ Create `advocate_clerk` table if it doesn't exist
CREATE TABLE IF NOT EXISTS advocate_clerk (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(128) NOT NULL,
    application_number VARCHAR(64) NOT NULL UNIQUE,
    advocate_id UUID NOT NULL REFERENCES advocate(id) ON DELETE CASCADE,
    is_active BOOLEAN DEFAULT TRUE,
    created_time BIGINT NOT NULL,
    last_modified_time BIGINT NOT NULL
);

-- ✅ Indexes for faster queries
CREATE INDEX IF NOT EXISTS idx_advocate_tenant ON advocate (tenant_id);
CREATE INDEX IF NOT EXISTS idx_advocate_application_number ON advocate (application_number);
CREATE INDEX IF NOT EXISTS idx_advocate_clerk_tenant ON advocate_clerk (tenant_id);
CREATE INDEX IF NOT EXISTS idx_advocate_clerk_application_number ON advocate_clerk (application_number);
