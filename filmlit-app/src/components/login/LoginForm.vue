<template>
	<div class="form-wrapper">
    	<img src="../icons/FilmLit_Logo_Morado-removebg-preview.png" alt="filmlit-logo" class="app-logo">
    	<h2>{{ formLoginView ? "Iniciar Sesion" : "Registrarse" }}</h2>

    	<form @submit.prevent="formSubmmit">
      		<label for="nombre_usuario">Nombre de Usuario:</label>
      		<input type="text" id="nombre_usuario" name="nombre_usuario" maxlength="150" v-model="nombre_usuario" required>

      		<label for="contrasena">Contraseña:</label>
      		<input type="password" id="contrasena" name="contrasena" maxlength="150" v-model="contrasena" required>

      		<label for="email" v-if="!formLoginView">Correo Electrónico:</label>
      		<input type="email" id="email" name="email" maxlength="255" v-model="email"required v-if="!formLoginView">

      		<button type="submit">{{ formLoginView ? "Ingresar" : "Registrarse" }}</button>

			<div v-if="mensajeError" class="mensaje-error">{{ mensajeError }}</div>
    </form>

	<div class="form-footer">
      	<a @click="cambiarForm()">{{ formLoginView ? "¿No tienes una cuenta? Registrate aquí" : "¿Ya tienes una cuenta? Inicia sesión" }}</a>
    </div>
  </div>
</template>

<script setup>
	import { ref } from 'vue';
	import { useRouter } from 'vue-router';
	import axios from 'axios';
	import Swal from 'sweetalert2';

	const nombre_usuario = ref('');
	const contrasena = ref('');
	const email = ref('');

	const mensajeError = ref('');

	const formLoginView = ref(true);

	// Este router nos permite hacer el cambio de URL
	const router = useRouter();

	const cambiarForm = async () => {
		formLoginView.value = !formLoginView.value;
		limpiarForm();
	}

	function limpiarForm () {
		nombre_usuario.value = '';
		contrasena.value = '';
		email.value = '';
		mensajeError.value = ''
	}

	const formSubmmit = async () => {
		if (formLoginView.value) {
			authLogin();
		} else {
			authRegistro();
		}
	}

	async function authLogin () {
		try {
			const params = new URLSearchParams();
			params.append('username', nombre_usuario.value);
			params.append('password', contrasena.value);

			const response = await axios.post('http://127.0.0.1:8000/token', params)

			// Toma el token de acceso y lo guarda en el localstorage para utilizarlo despues
			const token = response.data.access_token
			localStorage.setItem('token', token)
			console.log('Token agregado: ', localStorage.getItem('token'));

			router.push('/')
		} catch (error) {
			console.log('Error al iniciar sesion: ', error);
			mensajeError.value = error.response.data.detail;
		}
	}

	async function authRegistro() {
		try {
			const response = await axios.post('http://127.0.0.1:8000/registro', {
				nombre_usuario: nombre_usuario.value,
				contrasena: contrasena.value,
				email: email.value
			})

			Swal.fire({
					icon:'success',
					title: 'Registro Exitoso',
					text: response.data.mensaje,
					timer: 4000,
			});

			cambiarForm();
			limpiarForm();
		} catch (error) {
			console.log('Error al registrarse sesion: ', error);
			Swal.fire({
				icon: 'error',
				title: 'Error al Registrarse',
				text: error.response.data.detail,
			})
		}
	}
</script>

<style lang="scss" scoped>
	.app-logo {
		width: 70px;
		height: 70px;
		margin-bottom: 15px;
	}
  
	.form-wrapper {
		background-color: white;
		width: 100%;
		height: 100%;
		padding: 40px;
		box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
	}
  
	h2 {
		text-align: center;
		color: #333;
		margin-bottom: 20px;
		font-size: 24px;
	}
	
	label {
		font-size: 14px;
		font-weight: 500;
		color: #555;
		margin-bottom: 8px;
		display: block;
	}
	
	input[type="text"],
	input[type="password"],
	input[type="email"] {
		width: 100%;
		padding: 10px;
		margin: 8px 0 20px;
		border: 1px solid #ddd;
		border-radius: 6px;
		font-size: 16px;
		transition: border-color 300ms ease;
	}
	
	input[type="text"]:focus,
	input[type="password"]:focus,
	input[type="email"]:focus {
		border-color: #3a2064;
		outline: none;
	}
	
	button {
		width: 100%;
		padding: 12px;
		background-color: #6c63ff;
		color: white;
		border: none;
		border-radius: 6px;
		font-size: 16px;
		cursor: pointer;
		transition: background-color 300ms ease, transform 300ms ease;
	}
	
	button:hover {
		background-color: #4b45ef;
		transform: scale(1.1);
		transition: transform ease-in-out 300ms, background-color ease-in-out 300ms;
	}
	
	.form-footer {
		text-align: center;
		margin-top: 5px;
	}
	
	.form-footer a {
		color: #6c63ff;
		text-decoration: none;
		font-size: 14px;
		transition: font-size ease-in-out 300ms;
	}
	
	.form-footer a:hover {
		text-decoration: underline;
		font-size: 15px;
		cursor: pointer;
		transition: font-size ease-in-out 300ms;
	}

	.mensaje-error {
		color: red;
		margin-top: 5px;
		text-align: center
	}
</style>