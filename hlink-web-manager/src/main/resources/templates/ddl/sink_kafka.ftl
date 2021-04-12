<#setting classic_compatible=true>
CREATE TABLE ${tableName} (
<#list fields as field>
    ${field.name}  ${field.flinkType} <#if field_has_next>,</#if>
</#list>
) WITH (
'connector.type' = 'kafka',
'connector.version' = 'universal',
'connector.topic' = '${connector_topic}',
'connector.properties.0.key' = 'bootstrap.servers',
'connector.properties.0.value' = '${bootstrap_servers}',
'connector.property-version' = '1',
'format.type' = 'json',
'format.property-version' = '1',
'format.derive-schema' = 'true',
'connector.sink-partitioner'='${connector_sink_partitione}',
'update-mode' = 'append'
)