const express = require('express');
const vehiculosRoutes = require('./routes/vehiculos.routes');

const app = express();
const PORT = 3000;

app.use(express.json());

// Rutas de vehículos
app.use('/vehiculos', vehiculosRoutes);

// 404 básico
app.use((req, res) => {
  res.status(404).json({ mensaje: 'Ruta no encontrada' });
});

app.listen(PORT, () => {
  console.log(`Servidor escuchando en http://localhost:${PORT}`);
});
