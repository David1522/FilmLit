<template>
    <div class="modal-editar-sala">
        <div class="sala-contenedor">
            <h2 class="title">Editar Sala</h2>

            <form @submit.prevent="editarSala" class="form">
                <div class="img-container">
                    <div class="img-preview" v-if="imagenUrl">
                        <img :src="imagenUrl" alt="Imagen de la sala">
                    </div>
                    <div class="img-actions">
                        <label for="file-upload" class="btn-upload">Subir Imagen</label>
                        <input id="file-upload" type="file" accept="image/*" @change="guardarImagen" hidden />
                        <button v-if="sala.multimedia || multimedia" type="button" class="btn-delete" @click="borrarImagen">Borrar Imagen</button>
                    </div>
                </div>

                <div class="input-group">
                    <label for="nombre">Nombre</label>
                    <input id="nombre" v-model="sala.nombre" type="text" required />
                </div>

                <div class="input-group checkbox-group">
                    <input id="privado" v-model="sala.privado" type="checkbox" @change="togglePasswordInput" />
                    <label for="privado">Privado</label>
                </div>

                <div class="input-group" v-if="sala.privado">
                    <label for="contrasena">Contraseña</label>
                    <input id="contrasena" v-model="sala.contrasena" type="password" />
                </div>

                <div class="input-group">
                    <label for="descripcion_corta">Descripción</label>
                    <textarea id="descripcion_corta" v-model="sala.descripcion_corta" rows="4"></textarea>
                </div>

                <div class="btn-group">
                    <button type="submit" class="btn-primary">Guardar Cambios</button>
                    <button type="button" class="btn-secondary" @click="cerrarModal">Cancelar</button>
                </div>
            </form>
        </div>
    </div>
</template>
  
<script setup>
	import { computed, ref, watch, onUnmounted } from 'vue';
	import axios from 'axios';
	import Swal from 'sweetalert2';
	const props = defineProps({
		sala: Object,
	});
	
	const emits = defineEmits(['close', 'salaEditada']);
	
	const token = ref('');
	
	const sala = ref({ ...props.sala });
	const multimedia = ref(null);
	const objectUrl = ref(null);
	const eliminarMultimedia = ref(false); // Añadimos esta variable
	
	watch(() => props.sala, (newSala) => {
		sala.value = { ...newSala };
	}, { deep: true });
	
	const imagenUrl = computed(() => {
		if (multimedia.value) {
			if (objectUrl.value) URL.revokeObjectURL(objectUrl.value);
			objectUrl.value = URL.createObjectURL(multimedia.value);
			return objectUrl.value;
		}

		return sala.value.multimedia ? `http://localhost:8000/static/salas/${sala.value.multimedia}?${Date.now()}` : '';
	});
	
	function guardarImagen(event) {
		if (event.target.files.length > 0) {
			multimedia.value = event.target.files[0];
			eliminarMultimedia.value = false; // No eliminamos la imagen si hay una nueva seleccionada
		}
	}
	
	function borrarImagen() {
		multimedia.value = null;
		eliminarMultimedia.value = true; // Marcamos que queremos eliminar la imagen
		if (objectUrl.value) {
			URL.revokeObjectURL(objectUrl.value);
			objectUrl.value = null;
		}
		sala.value.multimedia = null; // También reseteamos el multimedia de la sala
	}
	
	async function editarSala() {
		const formData = new FormData();
		formData.append('id_perfil', sala.value.id_perfil);
		formData.append('nombre', sala.value.nombre);
		formData.append('privado', sala.value.privado);
		formData.append('contrasena', sala.value.contrasena);
		formData.append('descripcion_corta', sala.value.descripcion_corta);
		formData.append('eliminar_multimedia', eliminarMultimedia.value.toString()); // Asegurarse de añadir este campo
	
		if (multimedia.value) {
			formData.append('multimedia', multimedia.value);
		}
	
		try {
			validarToken();

			await axios.put(`http://localhost:8000/salas/${sala.value.id_sala}`, formData, {
				headers: {
					Authorization: `Bearer ${token.value}`,
					"Content-Type": "multipart/form-data"
				}
			});

			Swal.fire({
				title: 'Sala editada!',
				icon: 'success',
				text: 'La sala se edito satisfactoriamente!'
			});

			emits('salaEditada');
			cerrarModal();
		} catch (error) {
			console.error('Error al editar la sala:', error);
		}
	}
	
	function cerrarModal() {
		emits('close');
	}

	async function validarToken() {
        token.value = localStorage.getItem('token');
        if (!token.value) {
            router.push('login');
            return;
        }
    }
	
	onUnmounted(() => {
		if (objectUrl.value) URL.revokeObjectURL(objectUrl.value);
	});
</script>
  
<style scoped>
	.modal-editar-sala {
		position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background-color: var(--background-color-blur);
        backdrop-filter: blur(10px);
        -webkit-backdrop-filter: blur(10px);
        display: flex;
        align-items: center;
        justify-content: center;
		z-index: 9999;
	}

	.sala-contenedor {
		background: var(--background-color-primary);
        color: var(--color-text-primary);
        border: 2px solid var(--color-border);
		padding: 2rem;
		border-radius: 8px;
		width: 400px;
		box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
	}

	.title {
		font-size: 1.5rem;
		text-align: center;
		margin-bottom: 1rem;
	}

	.form {
		display: flex;
		flex-direction: column;
		gap: 1rem;
	}

	.img-container {
		text-align: center;
		margin-bottom: 1rem;
	}

	.img-preview img {
		width: 100px;
		height: 100px;
		object-fit: cover;
		border-radius: 8px;
	}

	.img-actions {
		display: flex;
		justify-content: center;
		gap: 0.5rem;
		margin-top: 0.5rem;
	}

	.btn-upload,
	.btn-delete {
		background: #ddd;
		color: #000;
		border: none;
		padding: 0.5rem 1rem;
		border-radius: 5px;
		cursor: pointer;
		font-size: 0.9rem;
	}

	.btn-delete {
		background: #ff4d4d;
		color: white;
	}

	.input-group {
		display: flex;
		flex-direction: column;
		gap: 0.3rem;
	}

	input, textarea {
		padding: 0.6rem;
		border: 1px solid #ccc;
		border-radius: 5px;
		font-size: 1rem;
		width: 100%;
	}

	.checkbox-group {
		display: flex;
		align-items: center;
		gap: 0.5rem;
	}

	.btn-group {
		display: flex;
		justify-content: space-between;
	}

	.btn-primary {
		background: #4CAF50;
		color: white;
		border: none;
		padding: 0.7rem;
		border-radius: 5px;
		cursor: pointer;
		width: 48%;
		font-size: 1rem;
	}

	.btn-secondary {
		background: #ccc;
		border: none;
		padding: 0.7rem;
		border-radius: 5px;
		cursor: pointer;
		width: 48%;
		font-size: 1rem;
	}
</style>