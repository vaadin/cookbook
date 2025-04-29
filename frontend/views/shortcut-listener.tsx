import "../recipe/shortcut-listener/shortcut-listener.js";
import Tag from "../generated/com/vaadin/recipes/recipe/Tag.js";
import type { ViewConfig } from "./_shared/ViewConfig.js";
export const config: ViewConfig = {
    title: "Listen for keyboard shortcuts",
    detail: {
        description: "Learn how to react to various shortcut keys with modifier keys and scopes.",
        tags: [Tag.TYPE_SCRIPT, Tag.HILLA, Tag.KEYBOARD],
    },
};
export default function ShortcutListener() {
    return <shortcut-listener />;
}
