local-env-create:
	docker-compose -f stack.yaml up -d
	sleep 1
	docker cp data/ddl.sql postgres-cardholder-api:/var/lib/postgresql/data
	docker exec postgres-cardholder-api psql -h localhost -U admin -d postgres -a -f ./var/lib/postgresql/data/ddl.sql

local-env-destroy:
	docker-compose -f stack.yaml down