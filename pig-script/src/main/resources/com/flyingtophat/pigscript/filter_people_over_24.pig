people = LOAD '$DATA_INPUT' USING AvroStorage();

people_over_24 = FILTER people BY age > 24;

STORE people_over_24 INTO '$DATA_OUTPUT' USING AvroStorage();