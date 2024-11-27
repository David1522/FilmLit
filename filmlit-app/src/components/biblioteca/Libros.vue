<template>
  <div class="scroll-container">
    <h1 style="color: var(--color-text-primary);">Libros Populares</h1>
    <ul v-if="books.length" class="book-list">
      <li v-for="book in books" :key="book.id" class="book-card">
        <div class="image-container">
          <img :src="book.volumeInfo.imageLinks?.thumbnail" alt="Portada del libro" class="book-image"/>
        </div>
        <div class="book-info">
          <h2 class="book-title">{{ truncateTitle(book.volumeInfo.title) }}</h2>
          <i class="fa-regular fa-heart"></i>
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
      hasSearched: false,
      startIndex: 0,
      maxResults: 20,
      loading: false
    };
  },
  methods: {
    generateRandomQuery(length = 2) {
      const characters = 'abcdefghijklmnopqrstuvwxyz';
      let randomQuery = '';
      for (let i = 0; i < length; i++) {
        randomQuery += characters.charAt(Math.floor(Math.random() * characters.length));
      }
      return randomQuery;
    },
    searchBooks(query = this.generateRandomQuery(), startIndex = this.startIndex) {
      const apiUrl = `https://www.googleapis.com/books/v1/volumes?q=${encodeURIComponent(query)}&startIndex=${startIndex}&maxResults=${this.maxResults}`;

      this.loading = true;
      axios.get(apiUrl)
        .then(response => {
          this.books = [...this.books, ...response.data.items || []];
          this.hasSearched = true;
          this.loading = false;
        })
        .catch(error => {
          console.error('Error al buscar libros:', error);
          this.loading = false;
        });
    },
    handleScroll() {
      const bottom = window.innerHeight + window.scrollY >= document.documentElement.scrollHeight;
      if (bottom && !this.loading) {
        this.startIndex += this.maxResults;
        this.searchBooks(this.query, this.startIndex);
      }
    },
    truncateTitle(title, maxLength = 20) {
      if (title.length > maxLength) {
        return title.substring(0, maxLength) + '...';
      }
      return title;
    }
  },
  mounted() {
    this.query = this.generateRandomQuery();
    this.searchBooks();
    window.addEventListener('scroll', this.handleScroll);
  },
  beforeDestroy() {
    window.removeEventListener('scroll', this.handleScroll);
  },
  name: 'BookSearch'
};
</script>

<style scoped>
.scroll-container {
    padding: 16px;
}

.book-list {
    display: flex;
    flex-wrap: wrap;
    gap: 16px;
    list-style: none;
    padding: 0;
    margin: 0;
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

.fa-regular {
  font-size: 20px;
  font-weight: 500;
  border: 1px solid var(--color-text-primary);
  border-radius: 50%;
  padding: 5px;
}

.fa-regular:hover {
  cursor: pointer;
  font-weight: 600;
}

.book-title {
    font-size: 16px;
    font-weight: bold;
    margin: 0;
}
</style>
