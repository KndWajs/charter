// Need to use the React-specific entry point to import createApi
import {createApi, fetchBaseQuery} from '@reduxjs/toolkit/query/react'
import CharterDto from "../shared/models/charterDto";

// Define a service using a base URL and expected endpoints
export const charterApi = createApi({
    reducerPath: 'charterApi',
    baseQuery: fetchBaseQuery({
        baseUrl: '/api/charter/'
    }),
    endpoints: (builder) => ({
        createCharter: builder.mutation<CharterDto, CharterDto>({
            query: (charter: CharterDto) => ({
                url: `create-charter`,
                method: "PUT",
                body: charter
            })
            ,
        }),
    }),
})

// Export hooks for usage in functional components, which are
// auto-generated based on the defined endpoints
// @ts-ignore
export const {useCreateCharterMutation} = charterApi
