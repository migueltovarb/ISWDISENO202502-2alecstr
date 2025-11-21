const express = require('express');
const router = express.Router();
const vehiculosController = require('../controllers/vehiculos.controller');

router.get('/', vehiculosController.obtenerTodos);
router.get('/:id', vehiculosController.obtenerPorId);
router.post('/', vehiculosController.crear);
router.put('/:id', vehiculosController.actualizar);
router.delete('/:id', vehiculosController.eliminar);

module.exports = router;
