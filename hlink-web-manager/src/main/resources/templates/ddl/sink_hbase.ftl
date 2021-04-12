<#setting classic_compatible=true>
CREATE TABLE ${tableName} (
<#list fields as field>
    ${field.name}  ${field.flinkType} <#if field_has_next>,</#if>
</#list>
with (
'connector.type' = 'hbase',
'connector.version' = '1.4.3',
'connector.table-name' = '${connector_table_name}',
'connector.zookeeper.quorum' = '${connector_zookeeper_quorum}'',
'connector.property-version' = '1',
'connector.zookeeper.znode.parent' = '${connector_zookeeper_znode_parent}',
'connector.write.buffer-flush.max-size' = '${connector_write_buffer_flush_max_size}',
'connector.write.buffer-flush.max-rows' = '${connector_write_buffer_flush_max_rows}',
'connector.write.buffer-flush.interval' = '${connector_write_buffer_flush_interval}'
)

<#--CREATE TABLE hbase_sink_table
(
rowkey VARCHAR,
cf row(
    add varchar,
    ver varchar
  )
)
with (
'connector.type' = 'hbase',
'connector.version' = '1.4.3',
'connector.table-name' = 'hbase_sink_table',
'connector.zookeeper.quorum' = 'xxxx:2181',
'connector.property-version' = '1',
'connector.zookeeper.znode.parent' = '/test',
'connector.write.buffer-flush.max-size' = '10mb',
'connector.write.buffer-flush.max-rows' = '1000',
'connector.write.buffer-flush.interval' = '2s'
)-->