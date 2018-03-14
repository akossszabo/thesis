<template>
  <div class="modal">
      <div class="modal-content">
        <form v-for="(field, index) in fields" :key="index">
          <div class="form-group">
            <input type="field.type" required="required" v-model="formdata[field.key]" />
            <label class="control-label" for="input">{{field.title}}</label><i class="bar"></i>
            <div class="invalid" v-show="validation[field.key] != null">{{validation[field.key]}}</div>
          </div>
        </form>

        <div class="button-container">
          <button class="button" type="button" @click="submit"><span>Submit</span></button>
          <button class="button" type="button" @click="cancel"><span>Cancel</span></button>
        </div>
      </div>
  </div>
</template>

<script>
export default {
  name: "form_modal",

  props: {
    fields: Array,
    formdata: Object
  },

  data() {
    return {
        validation: {},
        isAllValid: true
    };
  },

  methods: {
    submit() {
        this.validate();
        if(this.isAllValid) {
            this.$emit('onSubmitClick', this.formdata)
        }
    },
    cancel() {
        this.validation = {};
        this.isAllValid = true;
        this.$emit('onCancelClick');
    },
    validate() {
        this.validation = {};
        this.isAllValid = true;
        for (let f of this.fields) {
            if(f.validate) {
                if(f.validate.includes('email') && (!this.formdata[f.key] || !validateEmail(this.formdata[f.key]))) {
                    this.validation[f.key] = "This field must be a valid email";
                    this.isAllValid = false;
                }
                else if(f.validate.includes('required') && (!this.formdata[f.key] || this.formdata[f.key] === null || this.formdata[f.key] === '')) {
                    this.validation[f.key] = "This field is required";
                    this.isAllValid = false;
                }
                else if(f.validate.includes('numeric') && this.formdata[f.key] && (!isNumeric(this.formdata[f.key]))) {
                    this.validation[f.key] = "This field must only contain numeric characters";
                    this.isAllValid = false;
                }
                else if(f.validate.includes('min') && this.formdata[f.key]) {
                    var s = f.validate.substring(f.validate.indexOf("min") + 4);
                    var i = s.indexOf(")");
                    var number = s.substring(0,i);
                    if(this.formdata[f.key].length < number) {
                        this.validation[f.key] = "This field must be at least " + number + " characters";
                        this.isAllValid = false;
                    }
                }
                else if(f.validate.includes('max') && this.formdata[f.key]) {
                    var s = f.validate.substring(f.validate.indexOf("max") + 4);
                    var i = s.indexOf(")");
                    var number = s.substring(0,i);
                    if(this.formdata[f.key].length > number) {
                        this.validation[f.key] = "This field must not be greater than " + number + " characters";
                        this.isAllValid = false;
                    }
                }
            }
        }
    }
  }
};

function isNumeric(n) {
  return !isNaN(parseFloat(n)) && isFinite(n);
}

function validateEmail(email) {
    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(String(email).toLowerCase());
}
</script>

<style lang="scss" scoped>
@import "../styles/modal_form.scss";
.modal {
    position: fixed;
    z-index: 1;
    padding-top: 100px;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    overflow: auto;
    background-color: rgba(0,0,0,0.4);
}
.modal-content {
    position: relative;
    background-color: #fefefe;
    margin: auto;
    padding: 1em;
    border: 1px solid #888;
    width: 80%;
    box-shadow: 0 4px 8px 0 rgba(0,0,0,0.2),0 6px 20px 0 rgba(0,0,0,0.19);
    -webkit-animation-name: animatetop;
    -webkit-animation-duration: 0.4s;
    animation-name: animatetop;
    animation-duration: 0.4s
}
.button-container {
  text-align: center;
}
@-webkit-keyframes animatetop {
    from {top:-300px; opacity:0} 
    to {top:0; opacity:1}
}
@keyframes animatetop {
    from {top:-300px; opacity:0}
    to {top:0; opacity:1}
}
.invalid {
    font-size: 10px;
    color: red;
}
</style>