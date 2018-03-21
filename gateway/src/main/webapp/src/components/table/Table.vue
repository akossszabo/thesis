<template>
  <div class="maxheight unselectable" ref="tablepage">

    <div class="search-container">
      <span> Search: <input name="query" v-model="filterKey"> </span>

      <span v-for="item in filters" :key="item.key"> {{item.title}}:
        <select v-model="filterData[item.key]" @change="sortKey = sortKey + ' '"> <!-- WORKAROUND -->
            <option>All</option>
            <option v-for="i in item.selects" :key="i">{{i}}</option>
        </select>
      </span>

      <span style="float: right">
        <button class="newbutton" @click="$emit('onAddClick')">Add</button>
      </span>
      <span v-show="selectable" style="float: right">
        <button class="newbutton" @click="selectAllRows">All</button>
      </span>
      <span v-show="selectable && selectedRows.length > 0" style="float: right">
        <button class="newbutton" @click="clearSelectedRows">Clear</button>
      </span>
      <span v-show="selectable && deletable && selectedRows.length > 0" style="float: right">
        <button class="newbutton" @click="$emit('onDeleteClick')">Delete</button>
      </span>
      <span v-show="selectable && editable && selectedRows.length === 1" style="float: right">
        <button class="newbutton" @click="$emit('onEditClick')">Edit</button>
      </span>
    </div>

    <div class="table-container">
      <table>

        <thead>
          <tr :style="{ 'border-right-width': scollbarWidth*2 + 'px' }">
            <th v-for="item in headers" :key="item.key" @click="sortBy(item.key)" :style="{ width: item.width }" :class=item.hclasses>
              {{ item.title }}
              <span class="arrow" :class="{'asc': (sortKey === item.key && sortOrder === 1), 'dsc': (sortKey === item.key && sortOrder === -1)}"></span>
            </th>
          </tr>
        </thead>

        <tbody ref="tablebody" :style="{ height: bodyHeight + 'px' }">
          <tr class="datarow" 
              :class="{'scrollbar-exist': scollbarWidth > 0, 'selectedRow': selectable && selectedRowIds.includes(entry.id) }"
              v-for="(entry, index) in filteredData" :key="index" 
              @click="onRowSelected(entry); $emit('onRowClick', entry, selectedRows)"
              >
            <td v-for="header in headers" :key="header.key" :style="{ width: header.width }" :class=header.bclasses>
              <span :class="{ 'activeStatus': coloredStatus && header.key.toUpperCase() === 'STATUS' && entry[header.key].toUpperCase() === 'ACTIVE', 
                              'inactiveStatus': coloredStatus && header.key.toUpperCase() === 'STATUS'  && entry[header.key].toUpperCase() === 'INACTIVE' }">
                {{ header.prefix }}{{ entry[header.key] }}{{ header.postfix }}
              </span>  
            </td>
          </tr>
        </tbody>

      </table>
    </div>

    <div v-show="datas.length > 0" class="pager-container">
      <span>
        Show
        <select v-model="pageSizeText">
          <option>10</option>
          <option>25</option>
          <option>50</option>
          <option>all</option>
        </select>
        entries
      </span>
      <span style="float: right">
        <span class="page-span" v-show="selectable && selectedRows.length > 0"> Selected: {{ selectedRows.length }} </span>
        <span class="page-span"> {{ pageNumber }} / {{ maxPage }} [ {{ filteredLength }} ] </span>
        <button class="pager-button" @click="first">
          <i class="fa fa-angle-double-left" aria-hidden="true"></i>
        </button>
        <button class="pager-button" @click="prev">
          <i class="fa fa-angle-left" aria-hidden="true"></i>
        </button>
        <button class="pager-button" @click="next">
          <i class="fa fa-angle-right" aria-hidden="true"></i>
        </button>
        <button class="pager-button" @click="last">
          <i class="fa fa-angle-double-right" aria-hidden="true"></i>
        </button>
      </span>
    </div>

  </div>
</template>

<script src="table.js"></script>

<style lang="scss" scoped> 
@import "table.scss"; 
</style>
