<template>
    <div class="salas-container">
        <ul v-if="salas.length" class="salas-list">
			<SalaItem
				v-for="sala in salas"
				:key="sala.id_sala"
				:sala="sala"
				:user-id="userId"
				:registros-acceso="registrosAcceso"
				@salaEditada="actualizarDatos"
				@salaEliminada="fetchSalas"
			/>
        </ul>

        <p v-else class="no-salas">No hay salas disponibles.</p>
    </div>
</template>

<script setup>
	import { ref, onMounted, defineProps } from 'vue';
	import SalaItem from './SalaItem.vue';
	import axios from 'axios';

	const props = defineProps({
	userId: Number
	});

	const salas = ref([]);
	const registrosAcceso = ref([]);

	const fetchSalas = async () => {
	try {
		const token = localStorage.getItem('token');
		const response = await axios.get('http://localhost:8000/salas', {
		headers: { Authorization: `Bearer ${token}` }
		});
		salas.value = response.data;
	} catch (error) {
		console.error(error);
		alert('Error al cargar las salas.');
	}
	};

	const fetchRegistrosAcceso = async () => {
	try {
		const token = localStorage.getItem('token');
		const response = await axios.get('http://localhost:8000/mi_registros_acceso', {
		headers: { Authorization: `Bearer ${token}` }
		});
		registrosAcceso.value = response.data;
	} catch (error) {
		console.error(error);
		alert('Error al cargar los registros de acceso.');
	}
	};

	const actualizarDatos = async () => {
	await fetchSalas();
	await fetchRegistrosAcceso();
	};

	onMounted(() => {
	fetchSalas();
	fetchRegistrosAcceso();
	});
</script>


<style scoped>
	.salas-container {
	color: var(--color-text-primary);
	display: flex;
	flex-direction: column;
	align-items: center;
	overflow-y: auto;
	scrollbar-width: none;
	-ms-overflow-style: none;
	}

	.salas-container::-webkit-scrollbar {
	display: none;
	}

	.salas-list {
	width: 100%;
	list-style-type: none;
	padding: 0;
	}

	.no-salas {
	color: var(--color-text-secundary);
	font-size: 16px;
	padding-top: 15px;
	text-align: center;
	}
</style>
