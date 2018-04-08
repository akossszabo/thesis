<template>
  <div class="app-sidebar unselectable" :class="{'hidden-sidebar': !isSidebarVisible}">
    <div class="sidebar-wrapper">
      <div v-for="item in menuitems" :key="item.title">
        <div v-show="linksTitle" class="sidebar-linktitle" @click="menuClick(item.path)">
          {{item.title}}
        </div>
        <div :class="{'hidden': hiddableMenu && !openedMenus.includes(item.path), 'selected': sidebarTitle===child.title}" v-for="child in item.children" :key="child.title">
          <router-link :to="item.path+child.path">
            <div class="sidebar-link">{{child.title}}</div>
          </router-link>
        </div>
      </div>
    </div>
    <div class="lasts-wrapper">
      <div v-show="lastOpenedProjects.length > 0" class="sidebar-linktitle">Opened Projects</div>
      <div class="lasts-link" 
      v-for="project in lastOpenedProjects" :key="'p' + project.id" @click="toProject(project.id)"> {{project.name}} </div>
      
      <div v-show="lastOpenedIssues.length > 0" class="sidebar-linktitle">Opened Issues</div>
      <div class="lasts-link" 
      v-for="issue in lastOpenedIssues" :key="'i' + issue.id" @click="toIssue(issue.id)"> {{issue.name}} </div>
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";
export default {
  data() {
    return {
      hiddableMenu: false,
      linksTitle: false,
      menuitems: [
        { title: "Home",
          path: "",
          children: [{ title: "Dashboard", path: "/" }]
        },
        {
          title: "Trackings",
          path: "",
          children: [
            { title: "Projects", path: "/catalog/projects" },
            { title: "Issues", path: "/catalog/issues", }
          ]
        },
        {
          title: "People",
          path: "",
          children: [
            { title: "Users", path: "/catalog/registration" }
          ]
        }
      ],
      openedMenus: [""],
      resize: false
    };
  },
  computed: {
    ...mapState(["sidebarTitle", "isSidebarVisible", "lastOpenedProjects", "lastOpenedIssues"])
  },
  methods: {
    menuClick(input) {
      if (this.hiddableMenu && this.linksTitle) {
        if (this.openedMenus.includes(input)) {
          this.openedMenus = this.openedMenus.filter(title => title != input);
        } else {
          this.openedMenus.push(input);
        }
      }
    },
    toProject(id) {
      this.$router.push({ name: "toproject", params: { id: id } });
    },
    toIssue(id) {
      this.$router.push({ name: "toissue", params: { id: id } });
    },
  }
};
</script>

<style lang="scss" scoped>
@import "../styles/colors.scss";
.app-sidebar {
  border-right: 1px solid $sidebar-bordercolor;
  white-space: nowrap;
  background-color: white;
}
.hidden-sidebar {
  width: 0;
}
.sidebar-linktitle {
  padding-top: 5px;
  padding-left: 18px;
  font-size: 11px;
  font-weight: 700;
  cursor: pointer;
}
.sidebar-link {
  padding-bottom: 5px;
  padding-top: 5px;
  padding-left: 18px;
  font-size: 15px;
  color: $light-black;
  font-weight: 400;
  cursor: pointer;
  &:hover {
    background-color: $sidebar-hovercolor;
  }
}
.selected {
  background-color: $sidebar-selectedcolor;
}
.lasts-wrapper {
  background-color: $sidebar-lastscolor;
}
.lasts-link {
  padding: 5px;
  padding-left: 18px;
  font-size: 12px;
  font-weight: 500;
  cursor: pointer;
  font-weight: 400;
  &:hover {
    text-decoration: underline;
  }
}
.selected-link {
  font-weight: 600;
}
</style>