<template>
  <div class="app-container">
    <button id="myBtn" @click="showModal=true">Open Modal</button>
    <div v-show="showModal" id="myModal" class="modal">
      <div class="modal-content">
        <div class="modal-header">
          <span class="close" @click="showModal=false">&times;</span>
          <h2>Modal Header</h2>
        </div>

        <form>
          <div class="form-group">
            <input type="text" required="required" v-model="text1" />
            <label class="control-label" for="input">Textfield 1</label><i class="bar"></i>
          </div>

          <div class="form-group">
            <input type="text" required="required"/>
            <label class="control-label" for="input">Textfield 2</label><i class="bar"></i>
          </div>

          <div class="form-group">
            <select>
              <option v-for="o in options" :key="o.title"> {{ o.title }}</option>
            </select>
            <label class="control-label" for="select">Selectbox</label><i class="bar"></i>
          </div>
        </form>

        <div class="button-container">
          <button class="button" type="button" @click="submit"><span>Submit</span></button>
        </div>
      </div>
    </div>
  </div>
</template>


<script>
export default {
  data() {
    return {
      showModal: false,
      options: [
        { title: "Id" },
        { title: "Name" },
        { title: "Manufacturer" },
        { title: "Price" },
        { title: "Stock" },
        { title: "Status" },
        { title: "Adding date" }
      ],
      text1: ""
    };
  },

  methods: {
    submit() {
      console.log(this.text1);
      this.showModal = false;
    }
  }
};
</script>

<style lang="scss" scoped>
@import "../styles/modal_form.scss";
</style>