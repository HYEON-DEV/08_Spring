const axiosHelper = {
    ajax: async function (url, method, formData) {
        let response = null;

        try {
            switch (method.toLowerCase()) {
                case "get":
                    response = await axios.get(url, {
                        params: formData && Object.fromEntries(formData),
                    });
                    break;
                case "post":
                    response = await axios.post(url, formData);
                    break;
                case "put":
                    response = await axios.put(url, formData);
                    break;
                case "delete":
                    response = await axios.delete(url, {
                        data: formData,
                    });
                    break;
            }
        } catch (error) {
            let alertMsg = null;
            console.log(error);

            if (error.response?.data) {
                const data = error.response.data;
                alertMsg = data.message;

                console.error("Error occurred in the backend server.");
                console.error(`[${data.status}] ${data.error}`);
                console.error(data.trace);
            } else {
                alertMsg = error.message;

                console.error("Error occurred in the Axios.");
                console.error(`[${error.code}] ${error.message}`);
            }

            alert(alertMsg);
        }

        return response?.data;
    },
    get: async function (url, formData) {
        return await this.ajax(url, "get", formData);
    },
    post: async function (url, formData) {
        return await this.ajax(url, "post", formData);
    },
    put: async function (url, formData) {
        return await this.ajax(url, "put", formData);
    },
    delete: async function (url, formData) {
        return await this.ajax(url, "delete", formData);
    }
}