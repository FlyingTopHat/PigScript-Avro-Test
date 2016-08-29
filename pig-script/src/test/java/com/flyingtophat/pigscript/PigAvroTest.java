package com.flyingtophat.pigscript;

import com.flyingtophat.pigavrotest.avroschema.Person;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumWriter;
import org.apache.pig.pigunit.PigTest;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class PigAvroTest {

    private static final String PIG_SCRIPT = PigAvroTest.class.getResource("filter_people_over_24.pig").getPath();

    private static final String INPUT_DATA = "people";
    private static final String FILTERED_DATA = "people_over_24";

    private static final String DELIMITER = ",";

    private PigTest pigTest;

    @Before
    public void setUp() throws IOException {
        File dataFile = File.createTempFile("pig-avro-test", ".avro");
        writeAvroSchema(dataFile, Person.getClassSchema());

        final String[] arguments = {"DATA_INPUT=" + dataFile.toURI(), "DATA_OUTPUT=output"};
        pigTest = new PigTest(PIG_SCRIPT, arguments);
    }

    @Test
    public void return_25_year_old_when_provided_24_and_25_year_old() throws Throwable {
        final String[] input = {
            "Athos,24",
            "Porthos,25"
        };

        final String[] output = {
            "(Porthos,25)"
        };

        pigTest.assertOutput(INPUT_DATA, input, FILTERED_DATA, output, DELIMITER);
    }

    private static void writeAvroSchema(File file, Schema schema) throws IOException {
        DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<GenericRecord>(schema);
        DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<GenericRecord>(datumWriter);
        dataFileWriter.create(schema, file);
        dataFileWriter.close();
    }
}
