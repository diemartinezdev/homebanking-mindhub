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
        removeCard(cardId) {
                    Swal.fire({
                        title: 'Are you sure you want to delete this card?',
                        icon: 'warning',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn primary-btn btn-lg mb-3 mb-md-0',
                            cancelButton: 'btn secondary-btn btn-lg me-md-5 mb-3 mt-2 my-md-2'
                        },
                        showCancelButton: true,
                        confirmButtonText: 'Yes, delete this card',
                        cancelButtonText: 'Cancel',
                        reverseButtons: true
                    }).then(result => {
                        if (result.isConfirmed) {
                            axios.patch(`/api/clients/current/cards/${cardId}`)
                            .then(res => {
                                Swal.fire({
                                    position: 'center',
                                    icon: 'success',
                                    title: 'Your card has been deleted!',
                                    showConfirmButton: true,
                                    buttonsStyling: false,
                                    customClass: {
                                        confirmButton: 'btn primary-btn btn-lg',
                                    }
                                })
                                .then(result => {
                                    if (result.isConfirmed) {
                                        document.location.reload()
                                    }
                                })
                            })
                            .catch(error => {
                                console.log(error.response.data);
                                console.log(error.response.status);
                                console.log(error.response.headers);
                            })
                        }
                    })
                },
    },
    mounted: function () {
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
        this.getData();
    }
}).mount('#app')