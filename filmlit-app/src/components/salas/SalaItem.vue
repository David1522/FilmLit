<template>
	<li class="sala-item" ref="salaItemRef">
		<div class="sala-header">
			<div class="info-sala">
				<img :src="sala.multimedia ? `http://localhost:8000/static/salas/${sala.multimedia}?${Date.now()}` : 'http://localhost:8000/static/salas/pfp-icon.jpg'" alt="sala-image" class="sala-image">
				
				<div class="sala-info-text">
					<h2 class="sala-nombre">{{ sala.nombre }} <i id="candado" :class="sala.privado ? 'fas fa-lock' : 'fas fa-lock-open'"></i></h2>

					<p class="sala-anfitrion">Anfitrión: {{ sala.perfil.usuario.nombre_usuario }}</p>

					<p class="sala-descripcion">{{ sala.descripcion_corta }}</p>

					<button v-if="yaUnido" class="accion-btn" @click="entrarSala(sala.id_sala)">Entrar</button>

					<button v-else class="accion-btn" @click="unirmeSala">Unirme</button>
				</div>
			</div>

			<div class="sala-privado">
				<span>{{ sala.privado ? 'Privado' : 'Público' }}</span>
			</div>

			<div class="opciones">
				<OpcionesSala :is-own-sala="userId === sala.id_perfil" :id-sala="sala.id_sala" @editar-sala="abrirEditarSala" @eliminar-sala="confirmarEliminacion" @sala-reportada="reportarSala"/>
			</div>
		</div>

		<InfoSalaModal 
		v-if="mostrarInfoSala" 
		:sala="sala" 
		@close="cerrarInfoSala" 
		@actualizarSalas="actualizarDatos" 
		@registroAccesoObtenido="setRegistroAcceso"
		/>
		
		<EditarSalaModal 
		v-if="mostrarEditarSala" 
		:sala="sala" 
		@close="cerrarEditarSala" 
		@salaEditada="actualizarDatos" 
		/>
	</li>
</template>

<script setup>
	import OpcionesSala from './OpcionesSala.vue';

	import { ref, computed, onMounted, onBeforeUnmount, defineProps, defineEmits } from 'vue';
	import { useRouter } from 'vue-router';
	import axios from 'axios';
	import EditarSalaModal from '@/components/salas/EditarSalaModal.vue';
	import InfoSalaModal from '@/components/salas/InfoSalaModal.vue';
	import Swal from 'sweetalert2';

	const props = defineProps({
		sala: Object,
		userId: Number,
		registrosAcceso: {
			type: Array,
			required: true
		}
	});

	const emit = defineEmits(['salaEliminada', 'salaEditada']);
	const router = useRouter();
	const token = ref('');

	const mostrarOpciones = ref(false);
	const mostrarEditarSala = ref(false);
	const mostrarInfoSala = ref(false);
	const registroAcceso = ref(null);

	const yaUnido = computed(() => props.registrosAcceso.some(registro => registro.id_sala === props.sala.id_sala));

	const toggleDropdown = () => {
		mostrarOpciones.value = !mostrarOpciones.value;
	};

	const abrirEditarSala = () => {
		mostrarEditarSala.value = true;
	};

	const editarSala = () => {
		abrirEditarSala();
	};

	async function confirmarEliminacion() {
		Swal.fire({
                title: "¿Estas seguro que quieres eliminar esta sala?",
                icon: "info",
                text: "No podrás recuperarla y todo el chat será borrado",
                showCancelButton: true,
                focusConfirm: false,
                confirmButtonText: 'Confirmar',
                cancelButtonText: 'Cancelar',
            }).then((result) => {
                if (result.isConfirmed) {
                    eliminarSala();
                }
            });
	}

	async function eliminarSala ()  {
		try {
			await axios.delete(`http://localhost:8000/salas/${props.sala.id_sala}`, {
				headers: { 
					Authorization: `Bearer ${token.value}`
				}
			});

			Swal.fire({
				title: 'Sala eliminada con éxito!',
				icon: 'success',
				text: 'La sala ha sido eliminada con éxito.'
			});

			emit('salaEliminada');
		} catch (error) {
			console.error('Error al eliminar la sala:', error);

			Swal.fire({
				title: 'Error al eliminar la sala.',
				icon: 'error',
				text: error.response.data.detail
			});
		}
	};

	const reportarSala = () => {
		console.log('Reportar sala');
	};

	const cerrarEditarSala = () => {
		mostrarEditarSala.value = false;
	};

	const cerrarInfoSala = () => {
		mostrarInfoSala.value = false;
	};

	const entrarSala = () => {
		obtenerRegistroAcceso(props.sala.id_sala);
	};

	const obtenerRegistroAcceso = async (id_sala) => {
		validarToken();
		try {
			const response = await axios.get(`http://localhost:8000/registro_acceso/${id_sala}`, {
				headers: { 
					Authorization: `Bearer ${token.value}` 
				}
			});

			registroAcceso.value = response.data;
			mostrarChat();
		} catch (error) {
			console.error('Error al obtener el registro de acceso:', error);
		}
	};

	const mostrarChat = () => {
		router.push({ name: 'Chat', query: { registroAcceso: JSON.stringify(registroAcceso.value) } });
	};

	const unirmeSala = () => {
		mostrarInfoSala.value = true;
	};

	const actualizarDatos = () => {
		emit('salaEditada');
	};

	const handleClickOutside = (event) => {
		if (salaItemRef.value && !salaItemRef.value.contains(event.target)) {
			mostrarOpciones.value = false;
		}
	};

	const salaItemRef = ref(null);

	async function validarToken() {
        token.value = localStorage.getItem('token');
        if (!token.value) {
            router.push('login');
            return;
        }
    }

	onMounted(() => {
		document.addEventListener('click', handleClickOutside);
	});

	onBeforeUnmount(() => {
		document.removeEventListener('click', handleClickOutside);
	});
</script>


<style scoped>
	#candado{
		font-size: 15px;
		color: var(--color-text-secundary);
	}
	.sala-item {
		width: 80%;
		padding: 20px ;
		border-bottom: 1px solid var(--color-border);
		position: relative; /* Para posicionar el dropdown */
	}

	.sala-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding-right: 20px; /* Asegura un espacio adecuado para el icono de opciones */
	}

	.info-sala {
		display: flex;
		gap: 15px;
	}

	.sala-image {
		width: 120px;
		height: 120px;
		border-radius: 10%;
		object-fit: cover;
	}

	.sala-info-text {
		display: flex;
		flex-direction: column;
	}

	.sala-nombre {
		font-size: 20px;
		font-weight: 600;
	}

	.sala-anfitrion {
		font-size: 16px;
		font-weight: 400;
	}

	.sala-descripcion {
		font-size: 16px;
		word-break: break-all;
		color: var(--color-text-secundary);
	}

	.sala-privado {
		display: flex;
		align-items: center;
		gap: 5px;
		font-size: 16px;
		color: var(--color-text-secundary);
		margin-top: 10px;
		position: relative; /* Para posicionar los tres puntos */
	}

	.opciones {
		position: absolute;
		top: 20px; /* Ajusta según el diseño */
		right: 10px; /* Ajusta según el diseño */
		cursor: pointer;
	}

	.dropdown-menu {
		display: flex;
		flex-direction: column;
		position: absolute;
		top: 25px; /* Ajusta según el diseño */
		right: 0;
		background-color: var(--background-color-primary);
		border: 1px solid var(--color-border);
		border-radius: 8px;
		box-shadow: 0px 8px 16px rgba(0, 0, 0, 0.2);
		z-index: 1; /* Asegura que esté encima de otros elementos */
	}

	.dropdown-menu li {
		list-style: none;
	}

	.dropdown-menu button {
		white-space: nowrap; /* Previene el quiebre de línea para botones largos */
		background-color: transparent;
		color: var(--color-text-primary);
		border: none;
		padding: 10px;
		text-align: left;
		width: 100%;
		cursor: pointer;
	}

	.dropdown-menu button:hover {
		background-color: var(--background-color-secondary);
	}

	/* Estilo para el nuevo botón */
	.accion-btn {
		width: 75px;
		height: 30px;
		background-color: var(--background-color-primary);
		color: var(--color-text-primary);
		font-weight: 600;
		font-size: 14px;
		border: 1px solid var(--color-border);
		border-radius: 15px; /* Hace que el botón sea redondo/ovalado */
		margin-top: 30px; /* Añadir margen superior para separarlo de la descripción */
		display: flex;
		align-items: center;
		justify-content: center;
		gap: 5px;
		cursor: pointer;
		transition: background-color 200ms ease-in-out;
	}

	.accion-btn:hover {
		background-color: green;
		transition: background-color 200ms ease-in-out;
	}
</style>
