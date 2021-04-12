package com.haibao.tests.kafka;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.typeutils.RowTypeInfo;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

import java.util.Arrays;
import java.util.List;

/*
 * @Author ml.c
 * @Description
 * @Date 11:01 2020-04-10
 **/
public class UnnestedTest {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnvironment = StreamTableEnvironment.create(environment);


        List<Row> rows = Arrays.asList(
                Row.of(1, Row.of(66, "qq"),new Row[]{Row.of(12, "sd"), Row.of(15, "sd")}),
                Row.of(2, Row.of(66, "qq"),new Row[]{Row.of(13, "sd"), Row.of(16, "sd")}),
                Row.of(3, Row.of(66, "qq"),new Row[]{Row.of(14, "sd"), Row.of(17, "sd")})
        );

        String[] fieldNames = new String[]{"id","name"};
        TypeInformation<?>[]  subTypes = new TypeInformation[]{Types.INT,Types.STRING};

        TypeInformation<?>[] types = new TypeInformation[]{Types.INT, Types.ROW_NAMED(fieldNames,subTypes),Types.OBJECT_ARRAY(Types.ROW_NAMED(fieldNames,subTypes))};
//        TypeInformation<?>[] types = new TypeInformation[]{Types.INT, ObjectArrayTypeInfo.getInfoFor(new RowTypeInfo(Types.INT, Types.STRING))};
        String[] typeNames = new String[]{"a", "obj", "b"};

        DataStream<Row> source = environment
                .fromCollection(rows)
                .returns(new RowTypeInfo(types, typeNames));

        tableEnvironment.registerDataStream("source", source);

        Table a = tableEnvironment.sqlQuery("select a,obj.id,t.id,t.name from source,unnest(b) as t");

        tableEnvironment.toAppendStream(a, Row.class).print();

        environment.execute();
    }
}
