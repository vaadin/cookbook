import "../recipe/client-side-view-displaying-live-data/client-side-view-displaying-live-data.js";
import Tag from "../generated/com/vaadin/recipes/recipe/Tag.js";
import type { ViewConfig } from "./_shared/ViewConfig.js";
export const config: ViewConfig = {
    title: "Show real-time updating content in a client-side view",
    detail: {
        description: "Learn how to automatically update the UI with data from a reactive backend.",
        sourceFiles: ["com/vaadin/recipes/recipe/clientsidelivedata/StockDataEndpoint.java"],
        tags: [Tag.TYPE_SCRIPT, Tag.HILLA, Tag.PUSH],
    },
};
export default function ClientSideViewDisplayingLiveData() {
    return <client-side-view-displaying-live-data />;
}
