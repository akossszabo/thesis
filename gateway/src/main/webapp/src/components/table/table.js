export default {
    name: "apptable",

    props: {
        headers: Array,
        datas: Array,
        defaultSortKey: String,
        defaultPageSize: Number,
        selectable: Boolean,
        deletable: Boolean,
        editable: Boolean,
        coloredStatus: Boolean
    },

    data() {
        return {
            sortKey: "",
            sortOrder: 1,
            pageSize: 50,
            pageSizeText: "50",
            pageNumber: 1,
            maxPage: 0,
            filteredLength: 0,
            filterKey: "",
            bodyHeight: null,
            scollbarWidth: 0,
            selectedRows: [],
            selectedRowIds: []
        };
    },

    created() {
        this.sortKey = this.defaultSortKey || "";
        this.pageSize = this.defaultPageSize || 50;
        this.pageSizeText = this.pageSize;
    },

    beforeUpdate() {
        if (this.bodyHeight == null && this.$refs.tablepage && this.$refs.tablepage.clientHeight > 0) {
            this.onResize();
            window.addEventListener('resize', this.onResize);
        }
    },

    updated() {
        if(this.$refs.tablebody && this.$refs.tablebody.offsetWidth > 0) {
            this.scollbarWidth = this.$refs.tablebody.offsetWidth - this.$refs.tablebody.clientWidth;
        }
    },

    computed: {
        filteredData() {
            console.log("filtering and sorting...");
            var filterKey = this.filterKey && this.filterKey.toLowerCase();
            var sortKey = this.sortKey;
            var sortOrder = this.sortOrder;
            var data = this.datas;
            if (filterKey && filterKey.trim() != "") {
                data = data.filter(function (row) {
                    return Object.keys(row).some(function (key) {
                        return (
                            String(row[key])
                                .toLowerCase()
                                .indexOf(filterKey) > -1
                        );
                    });
                });
            }
            if (sortKey) {
                data = data.sort(function (o1, o2) {
                    let a = o1[sortKey];
                    let b = o2[sortKey];
                    if (!isNaN(a) && !isNaN(b)) {
                        return (a - b) * sortOrder;
                    }
                    return (a === b ? 0 : a > b ? 1 : -1) * sortOrder;
                });
            }
            this.filteredLength = data.length;
            this.maxPage = Math.ceil(this.filteredLength / this.pageSize);
            if (this.maxPage < 1) {
                this.maxPage = 1;
            }
            var from = this.pageSize * (this.pageNumber - 1);
            return data.slice(from, from + this.pageSize);
        }
    },

    methods: {
        sortBy(key) {
            if (this.sortKey === key) {
                this.sortOrder = this.sortOrder * -1;
            }
            this.sortKey = key;
        },
        prev() {
            if (this.maxPage !== 1) {
                this.pageNumber = this.pageNumber - 1;
                if (this.pageNumber < 1) {
                    this.pageNumber = 1;
                }
            }
        },
        next() {
            if (this.maxPage !== 1) {
                this.pageNumber = this.pageNumber + 1;
                if (this.pageNumber > this.maxPage) {
                    this.pageNumber = this.maxPage;
                }
            }
        },
        first() {
            this.pageNumber = 1;
        },
        last() {
            this.pageNumber = this.maxPage;
        },
        onResize() {
            this.bodyHeight = this.$refs.tablepage.clientHeight - 155;
        },
        onRowSelected(entry) {
            var id = entry.id;
            if(this.selectable) {
                if(this.selectedRowIds.includes(id)) {
                    this.selectedRowIds = this.selectedRowIds.filter(t => t != id);
                    this.selectedRows = this.selectedRows.filter(r => r.id != id);
                } else {
                    this.selectedRowIds.push(id);
                    this.selectedRows.push(entry);
                }
            }
        },
        clearSelectedRows() {
            this.selectedRowIds = [];
            this.selectedRows = [];
        },
        selectAllRows() {
            if(this.selectable) {
                this.filteredData.forEach(element => {
                    if(!this.selectedRowIds.includes(element.id)) {
                        this.selectedRowIds.push(element.id)
                        this.selectedRows.push(element)
                    }
                });
            }
        }
    },

    watch: {
        filterKey() {
            this.pageNumber = 1;
        },
        pageSizeText() {
            this.pageNumber = 1;
            if (this.pageSizeText === "all") {
                this.pageSize = this.filteredLength;
            } else {
                this.pageSize = Number(this.pageSizeText);
            }
        }
    },

    beforeDestroy() {
        window.removeEventListener('resize', this.onResize)
    }
};