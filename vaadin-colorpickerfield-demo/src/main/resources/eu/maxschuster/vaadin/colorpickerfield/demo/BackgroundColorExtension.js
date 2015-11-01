/**
 * Change the background color on the client-side from the server-side
 * 
 * @author Max Schuster
 */
function eu_maxschuster_vaadin_colorpickerfield_demo_BackgroundColorExtension() {
    
    var parent = this.getElement(this.getParentId());
    
    this.setBackgoundColor = function(backgroundColor) {
        parent.style.backgroundColor = backgroundColor;
    };
    
}
