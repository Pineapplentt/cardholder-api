CREATE TABLE IF NOT EXISTS BANK_ACCOUNT(
        bank_account_id uuid NOT NULL,
        account_number varchar(10) NOT NULL,
        agency_number varchar(4) NOT NULL,
        bank_code varchar(3) NOT NULL,
        created_at timestamp NOT NULL,
        PRIMARY KEY (bank_account_id)
);

CREATE TABLE IF NOT EXISTS CARD_HOLDER(
        card_holder_id uuid NOT NULL,
        client_id uuid NOT NULL,
        credit_analysis_id uuid NOT NULL,
        bank_account_fk_id uuid NOT NULL,
        card_holder_status varchar(8) NOT NULL,
        card_holder_limit decimal NOT NULL,
        card_holder_available_limit decimal NOT NULL,
        created_at timestamp NOT NULL,
        PRIMARY KEY (card_holder_id),
        FOREIGN KEY (bank_account_fk_id) REFERENCES BANK_ACCOUNT (bank_account_id)
);

CREATE TABLE IF NOT EXISTS CARD(
        card_id uuid NOT NULL,
        card_holder_id uuid NOT NULL,
        card_limit decimal NOT NULL,
        card_number varchar(16) NOT NULL,
        cvv integer NOT NULL,
        due_date date NOT NULL,
        created_at timestamp NOT NULL,
        PRIMARY KEY (card_id),
        FOREIGN KEY (card_holder_id) REFERENCES CARD_HOLDER (card_holder_id)
);
