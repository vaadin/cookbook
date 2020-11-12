const ContextMenu = customElements.get('vaadin-context-menu');
if (ContextMenu) {
    // We can store the original method, if we were to call it in our changed one, but here we simply don't.
    // const itemsRenderer = ContextMenu.prototype.__itemsRenderer;
    ContextMenu.prototype.__itemsRenderer = function  __itemsRenderer(root, menu, context) {
        this.__initMenu(root, menu);
        const subMenu = root.querySelector(this.constructor.is);
        subMenu.closeOn = menu.closeOn;
        const listBox = root.querySelector('vaadin-context-menu-list-box');
        listBox.innerHTML = '';
        const items = Array.from(context.detail.children || menu.items);
        items.forEach(item => {
            let component;
            if (item.component instanceof HTMLElement) {
                component = item.component;
            } else {
                component = document.createElement(item.component || 'vaadin-context-menu-item');
            }
            if (component instanceof ContextMenu) {
                component.setAttribute('role', 'menuitem');
                component.classList.add('vaadin-menu-item');
            } else if (component.localName === 'hr') {
                component.setAttribute('role', 'separator');
            }
            this.theme && component.setAttribute('theme', this.theme);
            component._item = item;
            if (item.text) {
                component.textContent = item.text;
            }
            this.__toggleMenuComponentAttribute(component, 'menu-item-checked', item.checked);
            this.__toggleMenuComponentAttribute(component, 'disabled', item.disabled);
            // The user may have set this already according to what happens when the menu action is executed;
            // example: may be set to 'dialog' when the action is to show a modal Dialog pop-up window.
            var ariaUnset = (null === component.getAttribute('aria-haspopup'));
            if (ariaUnset) {
                component.setAttribute('aria-haspopup', 'false');
            }
            component.classList.remove('vaadin-context-menu-parent-item');
            if (item.children && item.children.length) {
                component.classList.add('vaadin-context-menu-parent-item');
                component.setAttribute('aria-haspopup', 'true');
                component.setAttribute('aria-expanded', 'false');
                component.removeAttribute('expanded');
            }
            listBox.appendChild(component);
        });
    };
}
