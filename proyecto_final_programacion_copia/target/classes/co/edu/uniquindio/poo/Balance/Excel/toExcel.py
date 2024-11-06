import json


class toExcel:
    def __init__(self, json_path):
        self.json_path = json_path

    def load_data(self):
        """Carga los datos desde un archivo JSON."""
        try:
            with open(self.json_path, "r") as file:
                data = json.load(file)
            return data.get("listaEventos", []), data.get("listaPersonas", [])
        except FileNotFoundError:
            print(f"Error: No se encontró el archivo {self.json_path}")
            return [], []
