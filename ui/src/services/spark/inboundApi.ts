// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

const getList = async function (
  params: {
    name?: string;
    type?: string;
  },
  options?: { [key: string]: any },
) {
  return request<any>('/api/inbound/list', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
};

export async function createNew(params: any, options?: { [key: string]: any }) {
  return request<any>('/api/inbound/create_new', {
    method: 'post',
    data: {
      ...params,
    },
    ...(options || {}),
  });
}

const getAllInboundType = async function () {
  return request<API.KeyValue>('/api/inbound/get_types', {
    method: 'GET',
  });
};

const view = async function (
  params: {
    name: string;
  },
  options?: { [key: string]: any },
) {
  return request<any>('/api/inbound/view', {
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
  return request<any>('/api/inbound/disable', {
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
  getAllInboundType,
};
