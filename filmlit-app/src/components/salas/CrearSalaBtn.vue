<template>
	<button :style="{ visibility: esVisible ? 'visible' : 'hidden' }" @click="$emit('abrirModal')" class="crear-sala-btn">Crear <fa icon="plus"/></button>
</template>

<script setup>
	const props = defineProps({
		esVisible: {
			type: Boolean,
			default: true
		}
	});
</script>


<style scoped>
	.crear-sala-btn {
	justify-self: end;
	width: 75px;
	height: 30px;
	background-color: var(--background-color-primary);
	color: var(--color-text-primary);
	font-weight: 600;
	font-size: 14px;
	border: 1px solid var(--color-border);
	border-radius: 15px;
	margin: 10px;
	display: flex;
	align-items: center;
	justify-content: center;
	gap: 5px;
	cursor: pointer;
	transition: background-color 200ms ease-in-out;
	}

	.crear-sala-btn:hover {
	background-color: green;
	transition: background-color 200ms ease-in-out;
	}
</style>
