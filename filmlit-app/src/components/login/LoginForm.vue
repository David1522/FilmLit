<template>
    <div class="form-container">
        <h2>{{ formLoginView ? "Iniciar Sesion" : "Registrarse" }}</h2>

        <form @submit.prevent="authUsuario">
          <label for="nombre_usuario">Nombre de Usuario:</label>
          <input type="text" id="nombre_usuario" name="nombre_usuario" maxlength="150" v-model="nombre_usuario" required>

          <label for="contrasena">Contraseña:</label>
          <input type="password" id="contrasena" name="contrasena" maxlength="150" v-model="contrasena" required>

          <label for="email" v-if="!formLoginView">Correo Electrónico:</label>
          <input type="email" id="email" name="email" maxlength="255" v-model="email"required v-if="!formLoginView">

          <button type="submit">{{ formLoginView ? "Ingresar" : "Registrarse" }}</button>
        </form>

        <div class="form-footer">
            <a @click="cambiarForm()">{{ formLoginView ? "¿No tienes una cuenta? Registrate aquí" : "¿Ya tienes una cuenta? Inicia sesión" }}</a>
        </div>
  </div>
</template>

<script setup>
  import { ref,  onMounted } from 'vue';
  import axios, { Axios } from 'axios';
  import Swal from 'sweetalert2';

  const nombre_usuario = ref('');
  const contrasena = ref('');
  const email = ref('');


  const formLoginView = ref(true);

  const cambiarForm = async () => {
    formLoginView.value = !formLoginView.value;
    console.log(formLoginView.value)
  }

  const authUsuario = async () => {
    console.log('authUsuario function called');
    try {
      if (formLoginView.value) {
        const response = await axios.post('http://127.0.0.1:8000/login', {
          nombre_usuario: nombre_usuario.value,
          contrasena: contrasena.value
        });
        console.log('Ingreso Exitoso: ', response.data);
        Swal.fire({
          icon: 'success',
          title: 'Ingreso Exitoso',
          text: 'Bienvenido',
        })
      } else {
        console.log('Sending Axios request...');
        const response = await axios.post('http://127.0.0.1:8000/registro', {
          nombre_usuario: nombre_usuario.value,
          contrasena: contrasena.value,
          email: email.value
        });
        console.log('Registro Exitoso: ', response.data)
        Swal.fire({
          icon:'success',
          title: 'Registro Exitoso',
          text: '¡Bienvenido a nuestra plataforma!',
        })
      }
    } catch (error) {
        console.log('Error al iniciar sesion o resgistrarse usuario: ', error)
        Swal.fire({
          icon: 'error',
          title: 'Error al iniciar sesión o registrarse',
          text: 'Por favor, intenta de nuevo',
        })
    }
  }

  onMounted(async () => {
    
  })
</script>

<style lang="scss" scoped>
    body {
      font-family: 'Arial', sans-serif;
      background-color: #f0f0f0;
      display: flex;
      justify-content: center;
      align-items: center;
      height: 100vh;
      margin: 0;
    }
    
    .form-container {
      background-color: white;
      padding: 40px;
      border-radius: 12px;
      box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
      max-width: 400px;
      width: 100%;
    }
    
    h2 {
      text-align: center;
      color: #333;
      margin-bottom: 20px;
      font-size: 24px;
    }
    
    label {
      font-size: 14px;
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
      transition: border-color 0.3s ease;
    }
    
    input[type="text"]:focus,
    input[type="password"]:focus,
    input[type="email"]:focus {
      border-color: #6c63ff;
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
      transition: background-color 0.3s ease;
    }
    
    button:hover {
      background-color: #5752e6;
    }
    
    .form-footer {
      text-align: center;
      margin-top: 20px;
    }
    
    .form-footer a {
      color: #6c63ff;
      text-decoration: none;
      font-size: 14px;
    }
    
    .form-footer a:hover {
      text-decoration: underline;
      cursor: pointer;
    }
</style>