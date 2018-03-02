<template>
  <div class="app-sidebar unselectable" :class="{'hidden-sidebar': !isSidebarVisible}">
    <div class="sidebar-link-container" v-for="item in menuitems" :key="item.title">

      <div class="sidebar-link" @click="menuClick(item.path)">
        {{item.title}}
      </div>

      <div class="sidebar-sublink-container" 
        :class="{'hidden': hiddableMenu && !openedMenus.includes(item.path), 'selected': selectedMenu===item.path+child.path}" 
        v-for="child in item.children" :key="child.title">
        <router-link class="sidebar-sublink" :to="item.path+child.path">
          <span @click="selectedMenu=item.path+child.path">{{child.title}}</span>
        </router-link>
      </div>

    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";

export default {
  data() {
    return {
      hiddableMenu: true,
      menuitems: [
        {
          isLink: true,
          title: "Home",
          path: "/",
          children: []
        },
        {
          isLink: false,
          title: "Catalog",
          path: "/catalog",
          children: [
            { title: "Projects", path: "/projects" },
            { title: "Issues", path: "/issues" }
          ]
        },
      ],
      openedMenus: [""],
      selectedMenu: null
    };
  },

  mounted () {
    let path = window.location.hash.substring(1);
    let main = path.split("/")[1];
    if(main === "") {
      this.openedMenus.push(main);
    } else {
      this.openedMenus.push("/"+main);
    }
    this.selectedMenu = path;    
  },

  computed: {
    ...mapState(["isSidebarVisible"])
  },

  methods: {
    menuClick(input) {
      if(this.openedMenus.includes(input)) {
        this.openedMenus = this.openedMenus.filter(title => title != input);
      } else {
        this.openedMenus.push(input);
      }
    }
  }
};
</script>

<style lang="scss" scoped>
@import "../styles/colors.scss";
.hidden-sidebar {
  width: 0;
}

.sidebar-link-container {
  padding-bottom: 5px;
  padding-top: 5px;
}

.sidebar-sublink-container {
  padding-bottom: 5px;
  padding-top: 5px;
  &:hover {
    background-color: $vlight-grey;
  }
}
.selected {
  background-color: $vlight-grey;
}

.sidebar-link {
  padding-left: 18px;
  font-size: 14px;
  text-decoration: none;
  cursor: pointer;
  font-weight: 600;
}

.sidebar-sublink {
  @extend .sidebar-link;
  color: $light-black;
  font-weight: 500;
}
</style>
