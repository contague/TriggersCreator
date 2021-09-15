package TriggersCreator.util;

import java.sql.*;
import java.util.*;

public class TableProperties {

    //получаем список таблиц
    public List getListOfTables(){
        List<String> listOfTables = new ArrayList<>();

        try(Connection connection = new UtilConnection().getConnection()) {
            Statement statement = connection.createStatement();

            String command = "SELECT table_name FROM information_schema.tables\n" +
                    "WHERE table_schema NOT IN ('information_schema','pg_catalog');";
            ResultSet resultSet = statement.executeQuery(command);

            while (resultSet.next()){
                listOfTables.add(resultSet.getString("table_name"));
            }
        }
        catch (SQLException exception){
            exception.printStackTrace();
        }

        return listOfTables;
    }

    //получаем список полей и их типов у конкретной таблицы
    public Map getMapOfColumns(String tableName){
        Map<String, String> mapOfColumn = new HashMap<>();
        String columnName;
        String columnType;

        try(Connection connection = new UtilConnection().getConnection()) {
            Statement statement = connection.createStatement();
            String command = "select column_name,domain_name \n" +
                    "from information_schema.columns \n" +
                    "where table_name = " + "'" + tableName + "';";
            ResultSet resultSet = statement.executeQuery(command);
            while (resultSet.next()){
                columnName = resultSet.getString("column_name");
                columnType = resultSet.getString("domain_name");

                mapOfColumn.put(columnName, columnType);
            }
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }

        return mapOfColumn;
    }

    //определяем тип документа
    public int getTypeTable(String tableName){
        int result = 0;
        String typeOfColumn;

        Map<String, String> mapOfColumns = getMapOfColumns(tableName);
        typeOfColumn = mapOfColumns.get("type_tbl_" + tableName);
        if (typeOfColumn.equalsIgnoreCase("DOMAIN_TYPE_TBL_SPR")){
            result = 1;
        }
        else if (typeOfColumn.equalsIgnoreCase("DOMAIN_TYPE_TBL_DOC")){
            result = 2;
        }
        else if (typeOfColumn.equalsIgnoreCase("DOMAIN_TYPE_TBL_REG")){
            result = 3;
        }
        return result;
    }

    public static boolean checkTypeFieldLongString(String domainName){
        if (domainName.equalsIgnoreCase("DOMAIN_TEXT_1500")){
            return true;
        }
        else if (domainName.equalsIgnoreCase("DOMAIN_TEXT_500")){
            return true;
        }
        else if (domainName.equalsIgnoreCase("DOMAIN_STR_PAR")){
            return true;
        }
        else if (domainName.equalsIgnoreCase("DOMAIN_ZAPROS")){
            return true;
        }
        else
            return false;
    }

    public static boolean checkTypeFieldBlob(String domainName){
        if (domainName.equalsIgnoreCase("DOMAIN_IMAGE")){
            return true;
        }
        else if (domainName.equalsIgnoreCase("DOMAIN_IMAGE_SMALL")){
            return true;
        }
        else if (domainName.equalsIgnoreCase("DOMAIN_EXTMODULE_BLOB")){
            return true;
        }
        else return false;
    }

}
