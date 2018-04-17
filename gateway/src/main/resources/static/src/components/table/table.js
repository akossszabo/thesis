export default {
  name: "apptable",

  props: {
    headers: Array,
    datas: Array,
    filters: Array,
    defaultSortKey: String,
    defaultPageSize: Number,
    selectable: Boolean,
    deletable: Boolean,
    editable: Boolean,
    coloredStatus: Boolean,
    clickableCells: Boolean,
    heightDiff: Number
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
      filterData: [],
      bodyHeight: null,
      scollbarWidth: 0,
      selectedRows: [],
      selectedRowIds: [],
      heightDifference: null
    };
  },

  created() {
    this.sortKey = this.defaultSortKey || "";
    this.pageSize = this.defaultPageSize || 50;
    this.pageSizeText = this.pageSize;
    if(this.filters) {
      for (var i = 0; i < this.filters.length; i++) {
        this.filterData[this.filters[i].key] = "All";
      }
    }
    this.heightDifference = this.heightDiff || 140;
  },

  beforeUpdate() {
    if (this.bodyHeight == null && this.$refs.tablepage && this.$refs.tablepage.clientHeight > 0) {
      this.onResize();
      window.addEventListener('resize', this.onResize);
    }
  },

  updated() {
    if (this.$refs.tablebody && this.$refs.tablebody.offsetWidth > 0) {
      this.scollbarWidth = this.$refs.tablebody.offsetWidth - this.$refs.tablebody.clientWidth;
    }
  },

  computed: {
    filteredData() {
      var filterKey = this.filterKey && this.filterKey.toLowerCase();
      var filterData = this.filterData;
      var filters = this.filters;
      var sortKey = this.sortKey.trim();
      var sortOrder = this.sortOrder;
      var data = this.datas;
      if ((filterKey && filterKey.trim() != "") || (filters && filters.length > 0)) {
        data = data.filter(function (row) {
          var showRow = Object.keys(row).some(function (key) {
            return String(row[key]).toLowerCase().indexOf(filterKey) > -1;
          });
          for (var i = 0; i < filters.length; i++) {
            let filterKey = filters[i].key;
            let filterValue = filterData[filterKey].toLowerCase();
            if (filterValue != 'all' && filterValue != row[filterKey].toLowerCase()) {
              showRow = false;
            }
          }
          return showRow;
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
      this.bodyHeight = this.$refs.tablepage.clientHeight - this.heightDifference;
    },
    onRowSelected(entry) {
      var id = entry.id;
      if (this.selectable) {
        if (this.selectedRowIds.includes(id)) {
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
      if (this.selectable) {
        this.filteredData.forEach(element => {
          if (!this.selectedRowIds.includes(element.id)) {
            this.selectedRowIds.push(element.id)
            this.selectedRows.push(element)
          }
        });
      }
    },
    cellClick(key, value, row) {
      if(this.clickableCells) {
        this.$emit('onCellClick', key, value, row);
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
    },
    datas() {
      this.selectedRows = [];
      this.selectedRowIds = [];
    }
  },

  beforeDestroy() {
    window.removeEventListener('resize', this.onResize)
  }
};