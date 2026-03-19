-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 18-03-2026 a las 22:59:25
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `notas`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `itemstarea`
--

CREATE TABLE `itemstarea` (
  `id` int(11) NOT NULL,
  `notaid` int(11) NOT NULL,
  `descripcion_item` varchar(50) NOT NULL,
  `completado` int(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `itemstarea`
--

INSERT INTO `itemstarea` (`id`, `notaid`, `descripcion_item`, `completado`) VALUES
(3, 8, 'Pollo', 1),
(4, 8, 'Lechuga', 0),
(5, 9, 'Pollo', 1),
(6, 9, 'Lechuga', 0),
(7, 16, 'Patatas', 1),
(8, 17, 'Pollo', 0),
(9, 17, 'Patatas', 1),
(10, 17, 'Lechuga', 0),
(11, 7, 'Patatas', 1),
(12, 7, 'Pollo', 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `nota`
--

CREATE TABLE `nota` (
  `id` int(255) NOT NULL,
  `titulo` varchar(50) NOT NULL,
  `descripcion` varchar(50) NOT NULL,
  `tipo` varchar(10) NOT NULL,
  `cargatrabajo` int(50) NOT NULL,
  `id_trabajador` int(11) NOT NULL,
  `fecha` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `nota`
--

INSERT INTO `nota` (`id`, `titulo`, `descripcion`, `tipo`, `cargatrabajo`, `id_trabajador`, `fecha`) VALUES
(7, 'Lista de la compra', '123098142', 'Tarea', 2, 5, '2026-03-18'),
(8, 'Lista de la compra2', 'Esto es una lista de la compra', 'Tarea', 2, 4, '2026-03-17'),
(9, 'Lista de la compra3', 'Esto es una lista de la compra', 'Tarea', 2, 3, '2026-03-17'),
(10, 'Pepe', '1234', 'Nota', 0, 2, '2026-03-18'),
(11, '21345', '123', 'Nota', 0, 3, '2026-03-18'),
(12, '21345', '123', 'Nota', 0, 3, '2026-03-18'),
(13, '123', '1234', 'Nota', 0, 5, '2026-03-18'),
(14, '123', '1234', 'Nota', 0, 5, '2026-03-18'),
(15, '1234', '1234', 'Nota', 0, 5, '2026-03-18'),
(16, '1234', '124', 'Tarea', 5, 5, '2026-03-18'),
(17, '1235', '51', 'Tarea', 5, 5, '2026-03-18');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `id` int(255) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `foto` varchar(255) NOT NULL,
  `rol` int(1) NOT NULL,
  `dni` varchar(9) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`id`, `nombre`, `password`, `foto`, `rol`, `dni`) VALUES
(1, 'juan', '12345678', ' ', 1, '05938124F'),
(2, 'pepe', '112233445566', ' ', 0, '01234567E'),
(3, 'sadfa', 'adsf', ' ', 0, 'adfs'),
(4, 'afdsfasd', 'dasf', ' ', 1, 'adfsadfs2'),
(5, 'adfs', 'asdf', ' ', 0, 'adfs'),
(6, 'pepe2', 'pepe12345', '', 0, '111111');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `itemstarea`
--
ALTER TABLE `itemstarea`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `nota`
--
ALTER TABLE `nota`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`id`,`dni`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `itemstarea`
--
ALTER TABLE `itemstarea`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT de la tabla `nota`
--
ALTER TABLE `nota`
  MODIFY `id` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT de la tabla `usuario`
--
ALTER TABLE `usuario`
  MODIFY `id` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
