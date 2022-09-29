// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

const getList = async function (
  params: {
    name: string;
    type?: string;
  },
  options?: { [key: string]: any },
) {
  return request<any>('/api/outbound/list', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
};

export async function createNew(params: any, options?: { [key: string]: any }) {
  return request<any>('/api/outbound/create_new', {
    method: 'post',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

const getAllOutboundType = async function () {
  return request<API.KeyValue>('/api/outbound/get_all_outbound_type', {
    method: 'GET',
  });
};

const view = async function (
  params: {
    name: string;
  },
  options?: { [key: string]: any },
) {
  return request<any>('/api/outbound/view', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
};

const disable = async function (
  params: {
    name: string;
  },
  options?: { [key: string]: any },
) {
  return request<any>('/api/outbound/disable', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
};

export default {
  getList,
  view,
  disable,
  createNew,
  getAllOutboundType,
};
