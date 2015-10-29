function eu_maxschuster_vaadin_colorpickerfield_demo_BackgroundColorExtension() {
    
    var parent = this.getElement(this.getParentId());
    
    this.setBackgoundColor = function(backgroundColor) {
        parent.style.backgroundColor = backgroundColor;
    };
    
}
