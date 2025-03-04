<template>
    <div class="salas-container" @scroll="handleScroll">
        <ul v-if="salas.length" class="salas-list">
			<SalaItem
				v-for="sala in salas"
				:key="sala.id_sala"
				:sala="sala"
				:user-id="userId"
				:registros-acceso="registrosAcceso"
				@salaEditada="actualizarDatos"
				@salaEliminada="actualizarDatos"
			/>
        </ul>

        <p v-else class="no-salas">No hay salas disponibles.</p>
    </div>
</template>

<script setup>
	import { ref, onMounted, defineProps } from 'vue';
	import SalaItem from './SalaItem.vue';
	import axios from 'axios';
	import router from '@/router';

	const token = ref('');

	const salas = ref([]);
	const registrosAcceso = ref([]);

	const page = ref(1);
	const size = ref(10);
	const total = ref(0);
	const hasNext = ref(false);
	const cargandoSalas = ref(false);

	const props = defineProps({
		userId: Number
	});

	async function fetchSalas (page = 1) {
		try {
			validarToken();

			const response = await axios.get('http://localhost:8000/salas', {
				headers: { 
					Authorization: `Bearer ${token.value}`
				},
				params: {
					page,
					size: size.value,
				}
			});

			const { data, total: totalSalas, has_next } = response.data;

			salas.value = [...salas.value, ...data];
			total.value = totalSalas;
			hasNext.value = has_next;
		} catch (error) {
			console.error(error);
			localStorage.removeItem('token');
            router.push('/');
		}
	}

	async function cargarSalas() {
		if (!hasNext.value || cargandoSalas.value) return;

		cargandoSalas.value = true;
		page.value += 1;

		await fetchSalas(page.value);
		cargandoSalas.value = false;
	}

	function handleScroll() {
        const container = document.querySelector('.salas-container');
        const inferiorContainer = container.scrollHeight - container.scrollTop <= container.clientHeight + 200;

        if (inferiorContainer && hasNext.value) {
			console.log("Adding page")
            cargarSalas();
        }
    }

	async function fetchRegistrosAcceso () {
		try {
			validarToken();

			const response = await axios.get('http://localhost:8000/mi_registros_acceso', {
				headers: { Authorization: `Bearer ${token.value}` }
			});
			registrosAcceso.value = response.data;
		} catch (error) {
			console.error(error);
			localStorage.removeItem('token');
            router.push('/');
		}
	}

	async function actualizarDatos() {
		page.value = 1;
		salas.value = []
		registrosAcceso.value = [];
		fetchSalas(page.value);
		fetchRegistrosAcceso();
	}

	async function validarToken() {
        token.value = localStorage.getItem('token');
        if (!token.value) {
            router.push('login');
            return;
        }
    }

	defineExpose({
		actualizarDatos,
	});

	onMounted(() => {
		fetchSalas();
		fetchRegistrosAcceso();
	});
</script>

<style scoped>
	.salas-container {
		color: var(--color-text-primary);
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
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
	}

	.no-salas {
		color: var(--color-text-secundary);
		font-size: 16px;
		padding-top: 15px;
		text-align: center;
	}
</style>
