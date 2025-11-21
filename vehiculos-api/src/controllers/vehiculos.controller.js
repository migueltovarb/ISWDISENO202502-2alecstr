const fs = require('fs');
const path = require('path');

const DATA_PATH = path.join(__dirname, '..', 'data', 'vehiculos.json');

function leerVehiculos() {
  try {
    const data = fs.readFileSync(DATA_PATH, 'utf8');
    return JSON.parse(data || '[]');
  } catch (error) {
    console.error('Error leyendo vehiculos.json:', error);
    return [];
  }
}

function escribirVehiculos(vehiculos) {
  try {
    fs.writeFileSync(DATA_PATH, JSON.stringify(vehiculos, null, 2), 'utf8');
  } catch (error) {
    console.error('Error escribiendo vehiculos.json:', error);
  }
}

function generarId() {
  return (
    Date.now().toString(16) +
    Math.random().toString(16).substring(2, 10)
  );
}

const obtenerTodos = (req, res) => {
  const vehiculos = leerVehiculos();
  res.json(vehiculos);
};

const obtenerPorId = (req, res) => {
  const { id } = req.params;
  const vehiculos = leerVehiculos();

  const vehiculo = vehiculos.find(v => v.id === id);
  if (!vehiculo) {
    return res.status(404).json({ mensaje: 'Vehículo no encontrado' });
  }

  res.json(vehiculo);
};

const crear = (req, res) => {
  const { marca, modelo, anio, placa } = req.body;

  if (!marca || !modelo || !anio || !placa) {
    return res.status(400).json({
      mensaje: 'Faltan datos: marca, modelo, anio y placa son obligatorios'
    });
  }

  const vehiculos = leerVehiculos();

  const nuevoVehiculo = {
    id: generarId(),
    marca,
    modelo,
    anio,
    placa
  };

  vehiculos.push(nuevoVehiculo);
  escribirVehiculos(vehiculos);

  res.status(201).json(nuevoVehiculo);
};

const actualizar = (req, res) => {
  const { id } = req.params;
  const { marca, modelo, anio, placa } = req.body;

  const vehiculos = leerVehiculos();
  const index = vehiculos.findIndex(v => v.id === id);

  if (index === -1) {
    return res.status(404).json({ mensaje: 'Vehículo no encontrado' });
  }

  if (marca !== undefined) vehiculos[index].marca = marca;
  if (modelo !== undefined) vehiculos[index].modelo = modelo;
  if (anio !== undefined) vehiculos[index].anio = anio;
  if (placa !== undefined) vehiculos[index].placa = placa;

  escribirVehiculos(vehiculos);

  res.json(vehiculos[index]);
};

const eliminar = (req, res) => {
  const { id } = req.params;
  const vehiculos = leerVehiculos();

  const index = vehiculos.findIndex(v => v.id === id);
  if (index === -1) {
    return res.status(404).json({ mensaje: 'Vehículo no encontrado' });
  }

  const eliminado = vehiculos.splice(index, 1)[0];
  escribirVehiculos(vehiculos);

  res.json({
    mensaje: 'Vehículo eliminado correctamente',
    vehiculo: eliminado
  });
};

module.exports = {
  obtenerTodos,
  obtenerPorId,
  crear,
  actualizar,
  eliminar
};
