<template>
  <div class="table-wrapper maxheight unselectable" ref="tablepage">

    <div class="search-container">
      <div style="float:left;"> Search: <input name="query" v-model="filterKey"> </div>

      <div id="filters" v-for="item in filters" :key="item.key" style="float:left; padding-left: 5px;"> {{item.title}}:
        <select v-model="filterData[item.key]" @change="sortKey = sortKey + ' '"> <!-- WORKAROUND -->
            <option>All</option>
            <option v-for="i in item.selects" :key="i">{{i}}</option>
        </select>
      </div>

      <div class="table-button-container">
        <span v-show="selectable && editable && selectedRows.length === 1">
          <button id="editbtn" class="table-button1" @click="$emit('onEditClick')">Edit</button>
        </span>
        <span v-show="selectable && selectedRows.length > 0">
          <button id="openbtn" class="table-button1" @click="$emit('onOpenClick', selectedRows)">Open</button>
        </span>
        <span v-show="selectable && deletable && selectedRows.length > 0">
          <button id="deletebtn" class="table-button1" @click="$emit('onDeleteClick')">Delete</button>
        </span>
        <span v-show="selectable && selectedRows.length > 0">
          <button id="clearbtn" class="table-button1" @click="clearSelectedRows">Clear</button>
        </span>
        <span v-show="selectable">
          <button id="allbtn" class="table-button1" @click="selectAllRows">All</button>
          </span>
        <span>
          <button id="addbtn" class="table-button1" @click="$emit('onAddClick')">Add</button>
        </span>
      </div>
    </div>

    <div v-if="datas" class="table-container">
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
                              'inactiveStatus': coloredStatus && header.key.toUpperCase() === 'STATUS'  && entry[header.key].toUpperCase() === 'INACTIVE' }"
                    @click="cellClick(header.key, entry[header.key], entry)">
                {{ header.prefix }}{{ entry[header.key] }}{{ header.postfix }}
              </span>  
            </td>
          </tr>
        </tbody>

      </table>
    </div>

    <div class="pager-container">
      <span class="page-size-container">
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