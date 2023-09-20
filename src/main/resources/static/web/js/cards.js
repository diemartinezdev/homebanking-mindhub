Vue.createApp({
    data() {
        return {
            clientInfo: {},
            creditCards: [],
            debitCards: [],
            errorToats: null,
            errorMsg: null,
        }
    },
    methods: {
        getData: function () {
            axios.get("/api/clients/current")
                .then((response) => {
                    //get client ifo
                    this.clientInfo = response.data;
                    this.creditCards = this.clientInfo.cards.filter(card => card.type == "CREDIT");
                    this.debitCards = this.clientInfo.cards.filter(card => card.type == "DEBIT");
                })
                .catch((error) => {
                    this.errorMsg = "Error getting data";
                    this.errorToats.show();
                })
        },
        formatDate: function (date) {
            return new Date(date).toLocaleDateString('en-gb');
        },
        signOut: function () {
            axios.post('/api/logout')
                .then(response => window.location.href = "/web/index.html")
                .catch(() => {
                    this.errorMsg = "Sign out failed"
                    this.errorToats.show();
                })
        },
        deleteCard() {
                        if (this.selectedCard !== null) { // Verifica si se ha seleccionado una tarjeta
                            console.log(`Deleting card with ID: ${this.selectedCard}`);
                            axios.delete(`/api/cards/${this.selectedCard}`)
                                .then(() => {
                                    this.creditCards = this.creditCards.filter(c => c.id !== this.selectedCard);
                                    this.debitCards = this.debitCards.filter(c => c.id !== this.selectedCard);
                                })
                                .catch((error) => {
                                    this.errorMsg = "Error deleting card";
                                    this.errorToats.show();
                                });
                        } else {
                            console.error("No card selected");
                        }
                  },
    },
    mounted: function () {
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
        this.getData();
    }
}).mount('#app')