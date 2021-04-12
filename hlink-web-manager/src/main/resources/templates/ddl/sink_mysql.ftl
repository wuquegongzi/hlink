<#setting classic_compatible=true>
CREATE TABLE ${tableName} (
<#list fields as field>
    ${field.name}  ${field.flinkType} <#if field_has_next>,</#if>
</#list>
) WITH (
    'connector.type' = 'jdbc',
    'connector.url' = '${connector_url}',
    'connector.table' = '${table_name}',
    'connector.username' = '${connector_username}',
    'connector.password' = '${connector_password}',
    'connector.write.flush.max-rows' = '${write_max_rows}'
)