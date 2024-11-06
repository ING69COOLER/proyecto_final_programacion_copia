# main.py



# Ejecutar la exportación
from toExcel import toExcel
from ExcelExporter import ExcelExporter


if __name__ == "__main__":
    # Cargar los datos
    json_path = "proyecto_final_programacion_copia/src/main/java/co/edu/uniquindio/poo/Balance/Excel/datos.json"
    archivo_excel = "proyecto_final_programacion_copia/src/main/java/co/edu/uniquindio/poo/Balance/Excel/Eventos_Personas.xlsx"
    data_loader = toExcel(json_path)
    lista_eventos, lista_personas = data_loader.load_data()

    # Exportar a Excel si hay datos
    if lista_eventos and lista_personas:
        excel_exporter = ExcelExporter(lista_eventos, lista_personas, archivo_excel)
        excel_exporter.delete_existing_file()
        excel_exporter.create_excel_file()
    else:
        print("No hay datos para exportar a Excel.")
