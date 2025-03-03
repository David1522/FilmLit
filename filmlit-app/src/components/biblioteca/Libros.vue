<template>
  <div class="scroll-container" @scroll="handleScroll">
    <h2 style="color: var(--color-text-primary); margin-bottom: 15px;">Libros Populares</h2>

    <ul v-if="books.length" class="book-list">
      <li v-for="book in books" :key="book.id" class="book-card">
        <div class="image-container">
          <img 
            :src="book.volumeInfo.imageLinks?.thumbnail || 'https://via.placeholder.com/150'" 
            alt="Portada del libro" 
            class="book-image" 
          />
        </div>
        <div class="book-info">
          <h2 class="book-title">{{ truncateTitle(book.volumeInfo.title) }}</h2>
          <p class="book-author">
            Autor: {{ book.volumeInfo.authors?.[0] || 'Autor desconocido' }}
          </p>
          <p class="book-publ-date">
            Publicado: {{ book.volumeInfo.publishedDate || 'Fecha no disponible' }}
          </p>
          <p class="book-categories" v-if="book.volumeInfo.categories">
            Categorías: {{ book.volumeInfo.categories.join(', ') }}
          </p>
          <div class="favorite-container">
            <i class="fa-regular fa-heart" title="Añadir a favoritos"></i>
          </div>
        </div>
      </li>
    </ul>
    
    <p v-if="!books.length && hasSearched" class="no-results">No se encontraron resultados.</p>
  </div>
</template>

<script setup>
  import { ref, onMounted, onBeforeUnmount } from 'vue';
  import axios from 'axios';

  const query = ref('');
  const books = ref([]);
  const hasSearched = ref(false);
  const startIndex = ref(0);
  const maxResults = 20;
  const loading = ref(false);

  function generateRandomQuery(length = 2) {
    const characters = 'abcdefghijklmnopqrstuvwxyz';
    let randomQuery = '';
    for (let i = 0; i < length; i++) {
      randomQuery += characters.charAt(Math.floor(Math.random() * characters.length));
    }
    return randomQuery;
  }

  function searchBooks(searchQuery = generateRandomQuery(), start = startIndex.value) {
    const apiUrl = `https://www.googleapis.com/books/v1/volumes?q=${encodeURIComponent(searchQuery)}&startIndex=${start}&maxResults=${maxResults}`;

    loading.value = true;
    axios
      .get(apiUrl)
      .then((response) => {
        books.value = [...books.value, ...(response.data.items || [])];
        console.log(response.data);
        hasSearched.value = true;
        loading.value = false;
      })
      .catch((error) => {
        console.error('Error al buscar libros:', error);
        loading.value = false;
      });
  }

  function handleScroll() {
        const container = document.querySelector('.scroll-container');
        const inferiorContainer = container.scrollHeight - container.scrollTop <= container.clientHeight + 200;

        if (inferiorContainer) {
          startIndex.value += maxResults;
          searchBooks(query.value, startIndex.value);
        }
  }

  function truncateTitle(title, maxLength = 20) {
    return title.length > maxLength ? `${title.substring(0, maxLength)}...` : title;
  }

  onMounted(() => {
    query.value = generateRandomQuery();
    searchBooks();
  });

  </script>

<style scoped>
    .scroll-container {
        width: 100%;
        height: calc(100vh - 190px);
        overflow-y: auto;
        box-sizing: border-box;
        scrollbar-width: none;
    }

    .scroll-container::-webkit-scrollbar {
        display: none; /* Chrome, Safari, Edge */
    }

    /* Estilos para la lista de libros */
    .book-list {
        display: flex;
        align-items: center;
        justify-content: center;
        flex-wrap: wrap;
        gap: 16px;
        list-style: none;
        padding: 10px;
        margin: 0;
    }

    .book-card {
        width: 100%;
        max-width: 300px;
        height: 450px;
        border: 1px solid var(--color-border);
        border-radius: 12px;
        overflow: hidden;
        background-color: var(--background-color-primary);
        box-shadow: var(--box-shadow);
        transition: transform 0.3s;
    }

    .book-card:hover {
        transform: scale(1.05);
        cursor: pointer;
    }

    .image-container {
        overflow: hidden;
    }

    .book-image {
        width: 100%;
        height: 250px;
        object-fit: cover;
    }

    .book-info {
        padding: 10px;
        color: var(--color-text-primary);
    }

    .book-title {
        font-size: 16px;
        font-weight: bold;
        margin: 0 0 5px;
    }

    .book-author,
    .book-publ-date,
    .book-categories {
        font-size: 14px;
        margin: 0 0 5px;
    }

    .favorite-container {
        width: 100%;
        display: flex;
        justify-content: left;
    }

    .fa-regular {
        font-size: 20px;
        font-weight: 500;
        padding: 5px;
        cursor: pointer;
    }

    .fa-regular:hover {
        font-weight: 600;
    }

    .navbar {
        position: fixed;
        bottom: 0;
        width: 100%;
        height: 80px;
        background-color: var(--background-color-primary);
        z-index: 1000; /* Más alto que el contenido */
    }

    @media (max-width: 800px) {
        .scroll-container {
            height: calc(100vh - 355px);
        }
    }

    @media (max-width: 1030px) {
        .book-card {
            width: 100%;
            max-width: 250px;
        }
    }
</style>
