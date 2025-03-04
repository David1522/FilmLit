<template>
	<div class="chat-container">
		<div class="registro-info">
			<img :src="registroAcceso.sala.multimedia ? `http://localhost:8000/static/salas/${registroAcceso.sala.multimedia}?${Date.now()}` : 'http://localhost:8000/static/salas/pfp-icon.jpg'" alt="sala-image" class="sala-image">
			<div class="sala-info">
				<p class="sala-nombre"> {{ registroAcceso.sala.nombre }}</p>
				<p class="sala-descripcion"> {{ registroAcceso.sala.descripcion_corta }}</p>
			</div>
		</div>
	
		<div class="chat-box">
			<div class="messages" ref="messagesContainer">
				<p v-if="mensajes.length === 0" class="no-messages">No hay mensajes ahora mismo</p>
				<div v-for="mensaje in mensajes" :key="mensaje.id_mensaje" :class="{'message': true, 'message-mine': esMio(mensaje), 'message-other': !esMio(mensaje)}">
					<img :src="mensaje.registro_acceso.perfil.foto_perfil ? `http://localhost:8000/static/fotos_perfil/${mensaje.registro_acceso.perfil.foto_perfil}?${Date.now()}` : 'http://localhost:8000/static/fotos_perfil/pfp-icon.jpg'" alt="pfp-usuario" class="pfp-usuario">
					<div class="message-content">
						<span class="message-name">{{ mensaje.registro_acceso.perfil.usuario.nombre_usuario }}</span>
						<p class="message-text">{{ mensaje.descripcion }}</p>
						<p v-if="mensaje.multimedia"><img :src="`http://localhost:8000/static/mensajes/${mensaje.multimedia}`" alt="message-image" class="message-image"/></p>
						<span class="message-date">{{ new Date(mensaje.fecha).toLocaleString() }}</span>
					</div>
				</div>
			</div>

			<div class="message-input">
				<input
					type="text"
					placeholder="Escribe un mensaje..."
					v-model="nuevoMensaje"
					@keyup.enter="enviarMensaje"
				/>
				<button @click="enviarMensaje"><i class="fa-regular fa-paper-plane"></i></button>
			</div>
		</div>
    </div>
</template>
  
  
<script setup>
	import { ref, onMounted, nextTick } from 'vue';
	import axios from 'axios';

	const token = ref('');

	const props = defineProps({
		registroAcceso: {
			type: Object,
			required: true
		}
	});

	const mensajes = ref([]);
	const nuevoMensaje = ref('');
	const messagesContainer = ref(null);

	const fetchMensajes = async () => {
		validarToken();

		try {
			const response = await axios.get(`http://localhost:8000/mensajes/${props.registroAcceso.id_sala}`, {
			headers: {
				Authorization: `Bearer ${token.value}`
			}
			});
			mensajes.value = response.data.sort((a, b) => new Date(a.fecha) - new Date(b.fecha));
			scrollToBottom();
		} catch (error) {
			console.error('Error al obtener los mensajes:', error);
		}
	};

	const enviarMensaje = async () => {
		if (nuevoMensaje.value.trim() !== '') {
			validarToken();

			try {
				const response = await axios.post('http://localhost:8000/mensajes', {
					id_registro_acceso: props.registroAcceso.id_registro_acceso,
					descripcion: nuevoMensaje.value,
					multimedia: null
				}, {
					headers: {
					Authorization: `Bearer ${token.value}`
					}
				});
				console.log('Mensaje enviado:', response.data);
				nuevoMensaje.value = '';
				fetchMensajes();
			} catch (error) {
				console.error('Error al enviar el mensaje:', error);
			}
		}
	};

	const esMio = (mensaje) => mensaje.id_registro_acceso === props.registroAcceso.id_registro_acceso;

	const scrollToBottom = () => {
		nextTick(() => {
			if (messagesContainer.value) {
			messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight;
			}
		});
	};

	async function validarToken() {
        token.value = localStorage.getItem('token');
        if (!token.value) {
            router.push('login');
            return;
        }
    }

	onMounted(() => {
		fetchMensajes();
	});
</script>

<style scoped>
	::-webkit-scrollbar {
    	display: none;
  	}

	.chat-container {
		display: flex;
		flex-direction: column;
		align-items: center;
		margin: 20px;
		padding: 20px;
		border-radius: 10px;
		width: 80%;
	}

	.registro-info {
		width: 100%;
		padding: 15px;
		margin-bottom: 20px;
		display: flex;
		border-bottom: 1px solid var(--color-text-secundary);
	}

	.sala-image {
		width: 100px;
		height: 100px;
		border-radius: 10%;
		object-fit: cover;
	}

	.sala-info {
		margin: 20px;
	}

	.sala-nombre {
		color: var(--color-text-primary);
		font-size: 50px;
		font-weight: 600;
	}

	.sala-descripcion {
		color: var(--color-text-secundary);
	}

	.chat-box {
		width: 100%;
		padding: 15px;
		border-radius: 8px;
		display: flex;
		flex-direction: column;
		justify-content: space-between;
		height: 63vh;
	}

	.messages {
		flex: 1;
		overflow-y: auto;
		padding: 10px;
		background-color: var(--background-color-primary);
		border-radius: 8px;
		margin-bottom: 15px;
		display: flex;
		flex-direction: column;
		gap: 10px;
	}

	.message {
		display: flex;
		align-items: flex-start;
		gap: 10px;
	}

	.message-mine {
		flex-direction: row-reverse;
	}

	.message-content {
		background-color: var(--background-color-secondary);
		color: var(--color-text-primary);
		padding: 10px 15px;
		border-radius: 10px;
		word-wrap: break-word;
		overflow-wrap: break-word;
		position: relative;
		max-width: 75%;
		min-width: 150px;
		width: fit-content;
		box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
	}

	.message-mine .message-content {
		background-color: var(--color-accent);
	}

	.pfp-usuario {
		width: 40px;
		height: 40px;
		border-radius: 50%;
		object-fit: cover;
	}

	.message-name {
		font-weight: bold;
		color: var(--color-text-primary);
		margin-bottom: 5px;
	}

	.message-text {
		font-size: 15px;
		color: var(--color-text-primary);
		margin-bottom: 12px;
	}

	.message-image {
		max-width: 100%;
		height: auto;
		border-radius: 5px;
		margin-top: 5px;
	}

	.message-date {
		position: absolute;
		bottom: 5px;
		right: 10px;
		font-size: 12px;
		color: var(--color-text-secondary);
	}

	.message-input {
		display: flex;
		align-items: center;
		gap: 10px;
		justify-content: center;
	}

	.message-input input {
		width: 60%;
		padding: 10px;
		border-radius: 50px;
		background-color: var(--background-color-secondary);
		border: 1px solid var(--color-border);
		color: var(--color-text-primary);
		height: 40px;
		font-size: 16px;
	}

	.message-input button {
		background-color: var(--color-accent);
		color: var(--color-text-primary);
		border: none;
		border-radius: 50%;
		width: 40px;
		height: 40px;
		cursor: pointer;
		font-size: 18px;
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.no-messages {
		text-align: center;
		color: var(--color-text-secondary);
		font-size: 16px;
		margin: auto;
	}

	@media (max-width: 800px) {
		.chat-box {
			height: calc(100vh - 385px);
		}

		.chat-container {
			width: 100%;
			margin: 10px;
			padding: 10px;
		}

		.sala-info {
			margin: 10px;
		}

		.sala-nombre {
			font-size: 30px;
		}

		.message-input input {
			width: 70%;
		}
	}
</style>