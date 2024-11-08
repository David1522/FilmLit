<template>
    <div class="scroll-container">
      <h1 style="color: var(--color-text-primary);">Libros Populares</h1>
      <ul v-if="books.length" class="book-list">
        <li v-for="book in books" :key="book.id" class="book-card">
          <div class="image-container">
            <img :src="book.volumeInfo.imageLinks?.thumbnail" alt="Portada del libro" class="book-image"/>
          </div>
          <div class="book-info">
            <h2 class="book-title">{{ book.volumeInfo.title }}</h2>
            <span class="like-icon">❤️</span>
          </div>
        </li>
      </ul>
      
      <p v-if="!books.length && hasSearched" class="no-results">No se encontraron resultados.</p>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    data() {
      return {
        query: '',
        books: [],
        hasSearched: false
      };
    },
    methods: {
      searchBooks(query = this.query || 'ab') {
        const apiUrl = `https://www.googleapis.com/books/v1/volumes?q=${encodeURIComponent(query)}`;
  
        axios.get(apiUrl)
          .then(response => {
            this.books = response.data.items || [];
            this.hasSearched = true;
          })
          .catch(error => {
            console.error('Error al buscar libros:', error);
          });
      }
    },
    mounted() {
      this.searchBooks();
    },
    name: 'BookSearch'
  };
  </script>
  
  <style scoped>
  .scroll-container {
      overflow-y: auto;
  }
  
  .book-list {
      display: flex;
      flex-wrap: wrap;
      gap: 16px;
      list-style: none;
      padding: 0;
  }
  
  .book-card {
      position: relative;
      width: 200px;
      border-radius: 12px;
      overflow: hidden;
      box-shadow: var(--box-shadow);
      display: flex;
      flex-direction: column;
      background-color: var(--background-color-primary);
  }
  
  .image-container {
      flex-grow: 1;
      overflow: hidden;
  }
  
  .book-image {
      width: 100%;
      height: auto;
  }
  
  .book-info {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 8px;
      background-color: var(--background-color-primary);
      color: var(--color-text-primary);
  }
  
  .like-icon {
      font-size: 20px;
      color: red;
  }
  
  .book-title {
      font-size: 16px;
      font-weight: bold;
      margin: 0;
  }
  </style>
  