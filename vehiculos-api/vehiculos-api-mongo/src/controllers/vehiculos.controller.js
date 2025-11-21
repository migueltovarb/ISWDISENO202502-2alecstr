const Vehiculo = require('../models/vehiculo.model');

// Obtener todos
const obtenerTodos = async (req, res) => {
  try {
    const vehiculos = await Vehiculo.find();
    res.json(vehiculos);
  } catch (error) {
    res.status(500).json({ mensaje: 'Error al obtener vehículos' });
  }
};

// Obtener por ID
const obtenerPorId = async (req, res) => {
  try {
    const vehiculo = await Vehiculo.findById(req.params.id);
    if (!vehiculo) {
      return res.status(404).json({ mensaje: 'Vehículo no encontrado' });
    }
    res.json(vehiculo);
  } catch (error) {
    res.status(500).json({ mensaje: 'Error al obtener vehículo' });
  }
};

// Crear
const crear = async (req, res) => {
  try {
    const { marca, modelo, anio, placa } = req.body;

    const nuevoVehiculo = new Vehiculo({
      marca,
      modelo,
      anio,
      placa
    });

    await nuevoVehiculo.save();
    res.status(201).json(nuevoVehiculo);
  } catch (error) {
    res.status(500).json({ mensaje: 'Error al crear vehículo' });
  }
};

// Actualizar
const actualizar = async (req, res) => {
  try {
    const vehiculoActualizado = await Vehiculo.findByIdAndUpdate(
      req.params.id,
      req.body,
      { new: true }
    );

    if (!vehiculoActualizado) {
      return res.status(404).json({ mensaje: 'Vehículo no encontrado' });
    }

    res.json(vehiculoActualizado);
  } catch (error) {
    res.status(500).json({ mensaje: 'Error al actualizar vehículo' });
  }
};

// Eliminar
const eliminar = async (req, res) => {
  try {
    const eliminado = await Vehiculo.findByIdAndDelete(req.params.id);

    if (!eliminado) {
      return res.status(404).json({ mensaje: 'Vehículo no encontrado' });
    }

    res.json({ mensaje: 'Vehículo eliminado', vehiculo: eliminado });
  } catch (error) {
    res.status(500).json({ mensaje: 'Error al eliminar vehículo' });
  }
};

module.exports = {
  obtenerTodos,
  obtenerPorId,
  crear,
  actualizar,
  eliminar
};
