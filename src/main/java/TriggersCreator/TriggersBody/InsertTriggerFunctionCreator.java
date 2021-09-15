package TriggersCreator.TriggersBody;

import TriggersCreator.util.TableProperties;

import java.util.Map;

public class InsertTriggerFunctionCreator {

    public String createFunction(String tableName, String nameFieldPk){
        TableProperties tableProperties = new TableProperties();
        Map<String, String> fieldsMap = tableProperties.getMapOfColumns(tableName);
        int docType = tableProperties.getTypeTable(tableName);
        String nameFieldIdExtBase = "IDEXT_BASE_" + tableName;
        String nameFieldIdExtDataout = "IDEXT_DOUT_" + tableName;
        String nameFieldIdBase = "IDBASE_" + tableName;

        String result = "CREATE FUNCTION ADD_LOG_" + tableName + "_INS ()\n" +
                "RETURNS trigger \n" +

                "LANGUAGE 'plpgsql' \n" +
                "    COST 100 \n" +
                "    VOLATILE PARALLEL UNSAFE \n" +
                "AS $BODY$\n" +
                "DECLARE  id_xtism BIGINT ;\n" +
                "DECLARE  add_in_tism char ;\n" +
                "DECLARE  idext_base BIGINT ;\n" +
                "DECLARE  idext_data_out BIGINT ;\n";
        if (docType == 3){
            result += "DECLARE  add_in_tism_reg char ; \n";
        }
        result += "begin\n";
        result += "select VALUE_SETUP from SETUP where ID_SETUP=4 into add_in_tism; \n" +
                "if (add_in_tism='1') then   \n" +
                "else\n" +
                "   return null; /*не включено логгирование*/\n" +
                "end if; \n";
        if (docType == 3){
            result += "  select VALUE_SETUP from SETUP where ID_SETUP=6 into add_in_tism_reg; \n" +
                    "if (add_in_tism_reg='1') then   \n" +
                    "else\n" +
                    " return;  /*не включено логгирование регистров*/ \n" +
                    "end if;\n";
        }
        result = result + "   idext_base=0; \n";
        result = result + "   idext_data_out=0; \n";
        result = result + "if (current_user='EXTUSER') then  \n";
        result = result + "      /*внешнее изменение*/\n";
        result = result + "          idext_base=new." + nameFieldIdExtBase + "; \n";
        result = result + "          idext_data_out=new." + nameFieldIdExtDataout + "; \n";
        result = result + "     end if;\n";

        result = result + "  \n" ;
        result = result + "  id_xtism=gen_id(GEN_XTISM_ID,1); \n";
        result = result + "  \n" ;

        result = result+"           insert into xtism (xtism.id_xtism,  \n";
        result = result+"                       xtism.operation_xtism ,   /*операция 1 вставка 2 изменение 3 удаление 4 отмена 5 проведение*/    \n";
        result = result+"                       xtism.type_object_xtism , /*тип объекта 1 справочник 2 документ 3 регистр*/  \n ";
        result = result+"                       xtism.name_table_xtism ,  /*имя таблицы*/             \n ";
        result = result+"                       xtism.name_field_id_xtism , /*имя поля первичного ключа*/  \n ";
        result = result+"                       xtism.value_field_id_xtism ,/* значение первичного ключа*/  \n";
        result = result+"                       xtism.idbase_xtism,         /* id базы владельца, для фильтрации */   \n";
        result = result+"                       xtism.idext_base_xtism,         /* id внешней базы для квитанции */   \n";
        result = result+"                       xtism.idext_dataout_xtism)         /* id_data_out для квитанции при внешнем обновлении */   \n";

        result = result+"              values (id_xtism,  \n";
        result = result+"                     1, \n ";
        result = result+"                    " + docType + ", \n";
        result = result+"                     '" + tableName + "', \n";
        result = result+"                     '" + nameFieldPk + "', \n";
        result = result+"                     new." + nameFieldPk + ", \n";
        result = result+"                     new." + nameFieldIdBase + ", \n";
        result = result+"                     idext_base, \n";
        result = result+"                     idext_data_out); \n";

        result = result+"	   \n ";

        for (Map.Entry<String, String> entry : fieldsMap.entrySet()){
            String fieldName = entry.getKey();
            String fieldType = entry.getValue();
            if(fieldName.equalsIgnoreCase("IDEXT_BASE_" + tableName)
                    || fieldName.equalsIgnoreCase("IDEXT_DOUT_" + tableName)){
                continue;
            }
            result = result + "if (not(new." + fieldName + " is null)) then  \n";
            result = result + "   insert into xtism_fields (xtism_fields.idxtism_xtism_fields,  \n";
            result = result + "                             xtism_fields.field_name_xtism_fields, \n";
            result = result + "                             xtism_fields.old_value_xtism_fields, \n";
            result = result + "                             xtism_fields.new_value_xtism_fields, \n";
            result = result + "                             xtism_fields.type_xtism_fields) \n";

            result = result + "                     values (id_xtism,  \n";
            result = result + "                             '" + fieldName + "',\n";

            //старое значение
            result = result + "                             null, \n";


            //новое значение

            if (TableProperties.checkTypeFieldLongString(fieldType)){
                //длинная строка
                result=result + "                             null, \n";
                result=result + "                             2); \n";      //тип поля
                result=result + "end if;";
            }
            else if (TableProperties.checkTypeFieldLongString(fieldType)){
                //BLOB поле
                result=result + "                             null, \n";
                result=result + "                             3); \n";      //тип поля
                result=result + "end if;";
            }
            else
            { //обычное поле
                result=result+"                             new." + fieldName + ", \n";
                result=result+"                             1); \n";    //тип поля
                result=result + "end if;";
            }
            result=result+"	   \n ";

        }
        result=result+"return new; \n";
        result=result+"end \n" +
                "$BODY$;" ;
        return result;
    }
}
