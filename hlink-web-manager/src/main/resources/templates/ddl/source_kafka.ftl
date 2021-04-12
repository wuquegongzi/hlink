<#setting classic_compatible=true>
CREATE TABLE ${tableName} (
<#list fields as field>
    ${field.name}  ${field.flinkType} <#if field_has_next>,</#if>
</#list>
) WITH (
'connector.type' = 'kafka',
'connector.version' = 'universal',
'connector.topic' = '${connector_topic}',
'update-mode' = 'append',
'connector.properties.0.key' = 'zookeeper.connect',
'connector.properties.0.value' = '${zookeeper_connect}',
'connector.properties.1.key' = 'bootstrap.servers',
'connector.properties.1.value' = '${bootstrap_servers}',
'connector.properties.2.key' = 'group.id',
'connector.properties.2.value' = '${group_id}',
'connector.startup-mode' = '${startup_mode}',
<#if startup_mode == "specific-offsets" && partition_offset?length gt 0 && partition_offset?contains(",")>
    <#list partition_offset?split(";") as subgroup>
        <#list subgroup?split(",") as po>
            <#if po_index == 0>
                'connector.specific-offsets.${subgroup_index}.partition' = ${po},
            </#if>
            <#if po_index == 1>
                'connector.specific-offsets.${subgroup_index}.offset' = ${po},
            </#if>
        </#list>
    </#list>
</#if>
'format.type' = 'json',
'format.fail-on-missing-field' = 'true',
'format.derive-schema' = 'true'
)
