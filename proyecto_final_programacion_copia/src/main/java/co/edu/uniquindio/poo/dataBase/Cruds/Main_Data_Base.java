package co.edu.uniquindio.poo.dataBase.Cruds;
import java.util.ArrayList;

public class Main_Data_Base implements IMBD{
   private static Main_Data_Base data_Base;
   private static ArrayList<IMBD> tablas = new ArrayList<IMBD>();
 
   public Main_Data_Base() {}

   public static Main_Data_Base getInstance() {
      if (data_Base == null) {
         data_Base = new Main_Data_Base();
         create_Data_Base();

      }
      return data_Base;
   }

   public static void addTable(IMBD table) {
      tablas.add(table);
   }

   private static void create_Data_Base() {
    for (IMBD table : tablas) {
       IMBD DB = table.createTable();
       addTable(DB);
    }
   }
   

}